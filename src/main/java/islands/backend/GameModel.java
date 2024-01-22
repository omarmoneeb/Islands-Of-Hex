package islands.backend;
/**
 * Class to model the play of the game
 *
 */
public class GameModel {
    public static class QuickFindUF {

        public static int[] id2;
        public static int[] id;
        static int size;
        public static int islands;
        /**
         * Initializes an empty union-find data structure with
         * {@code n} elements {@code 0} through {@code n-1}.
         * Initially, each element is in its own set.
         * @param  n the number of elements
         * @throws IllegalArgumentException if {@code n < 0}
         */
        public QuickFindUF(int n) {
            id = new int[n*n];
            id2= new int[n*n];
            size = n;
            int positionI;
            for (positionI = 0; positionI < size; positionI++){
                int positionJ;
                for(positionJ =0; positionJ <size; positionJ++){
                    int position = (positionI * size) + positionJ;
                    id[position]=0;
                    id2[position]= position;
                }
            }
        }
        /**
         * Returns the canonical element of the set containing element {@code p}.
         * @param  x an element
         * @return the canonical element of the set containing {@code p}
         */
        private int find(int x) {
            int z= Math.abs(x);
            return id2[z];
        }

        /**
         * Checking the surrounding of an island, so it will connect them together.
         */
        public void checking(int position,boolean clr){
            int color = clr ? -1:1;
            int[] directions = {position-size, position+size, position-1, position+1, (position-size)-1, (position + size)+1};
            for (int newP : directions) {
                if ((newP >= 0 && newP < (size * size))) {
                    if (id[newP] == (clr ? -1 : 1)) {
                        union(position, newP, color);
                    }
                }
            }
        }

        /**
         * Return weather if both P and Q have the same parent.
         * */
        public boolean connected(int p, int q){
            return find(p)==find(q);
        }

        /**
         * It goes over the top row and bottom row for WHITE, and checks if they are same and ends the game.
         * and does the same for the left col and the right col for BLACK.
         * */
        private boolean isGameOver(boolean color) {
            for(int i=0; i<size; i++){
                if(color == WHITE && (id[i]!=i)){
                    for(int j=0; j<size; j++){
                        if(id[size*(size-1)+j]==id[i]){
                            return connected(size*(size-1)+j, i);
                        }
                    }
                }
                if(color == BLACK && (id[size*i]!=size*i)){
                    for(int k=0; k<size; k++){
                        if(id[(size*i)+(size-1)]==id[size*k]){
                            return connected((size*i)+(size-1),size*k);

                        }
                    }
                }
            }
            return false;
        }


        /**
         * Merges the set containing element {@code p} with the set
         * containing element {@code q}.
         * @param  p one element
         * @param  q the other element
         * once the islands connected it decrements the score.
         */
        public void union(int p, int q,int color){
            int pID = find(p);
            int qID = find(q);
            if(pID == qID){
                id2[p] = id2[q];
                QuickFindUF.islands--;
                id[p] = color;
                id[q] = color;
            }
        }
    }



    public static final boolean WHITE = true;
    public static final boolean BLACK = false;
    QuickFindUF quickFind;
    int whiteScore=0;
    int blackScore=0;




    /**
     * Construct a game with given size x size and an empty game board
     * @param sz the square size of the board
     */
    public GameModel(int sz) {
        quickFind = new QuickFindUF(sz);

    }

    /**
     * Can a play be made at position row, col
     * @param row the row in question
     * @param col the col in question
     * @return true if row, col is empty, false o.w.
     * @throws IllegalArgumentException for invalid row and col
     */
    public boolean canPlay(int row, int col) {
        int size = QuickFindUF.size;
        int board = (row*size)+col;
        if (row < 0 || row >= size) {
            throw new IllegalArgumentException("Invalid row value.");
        }
        else if (col < 0 || col >= size) {
            throw new IllegalArgumentException("Invalid col value");
        }
        return QuickFindUF.id2[board] == board;
    }

    /**
     * play a piece and report if the game is over (true) false, otherwise
     * @param row the row where a piece is played
     * @param col the col where a piece is played
     * @param clr -1 for WHITE and 1 for BLACK
     * @return true if the game is over and false otherwise
     * @throws IllegalArgumentException for invalid row and col
     */
    public boolean makePlay(int row, int col, boolean clr) {

        int position = (row* QuickFindUF.size) +col;
        if(canPlay(row, col)) {
            if(clr&& QuickFindUF.id2[position]==position){
                QuickFindUF.id[position]= -1;
                QuickFindUF.id2[position]= -1;
            }
            else if(QuickFindUF.id2[position]==position) {
                QuickFindUF.id[position]=1;
                QuickFindUF.id2[position]=1;
            }
        }
        QuickFindUF.islands++;
        quickFind.checking(position,clr);



        if(clr == WHITE){whiteScore += QuickFindUF.islands; QuickFindUF.islands=0;}
        if(clr == BLACK){blackScore += QuickFindUF.islands; QuickFindUF.islands=0;}
        return quickFind.isGameOver(clr);
    }

    /**
     * Return the score for white
     * @return white score
     */
    public int whiteScore() {
        return whiteScore;
    }

    /**
     * return the score for black
     * @return black score
     */
    public int blackScore() {
        return blackScore;
    }

}