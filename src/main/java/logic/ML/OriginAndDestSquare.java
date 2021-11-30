package logic.ML;

import logic.enums.Square;

public class OriginAndDestSquare {
        private final Square origin;
        private final Square dest;

        public OriginAndDestSquare(Square x, Square y) {
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
