package logic.ML;

public class originAndDest<Square> {
        public final Square origin;
        public final Square dest;

        public originAndDest(Square x, Square y) {
            this.origin = x;
            this.dest = y;
        }

        public Square getOrigin() {
            return origin;
        }

        public Square getDest() {
            return dest;
        }
    }
