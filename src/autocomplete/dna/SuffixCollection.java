package autocomplete.dna;

import java.util.AbstractCollection;
import java.util.Iterator;

/**
 * Generates all (<i>length - 1</i>) suffixes of the given sequence of characters and presents them in a collection.
 */
class SuffixCollection extends AbstractCollection<CharSequence> {
    private final CharSequence data;

    /**
     * Constructs an unmodifiable collection of (<i>length - 1</i>) suffixes from the data.
     *
     * @param data source for all suffixes.
     */
    public SuffixCollection(CharSequence data) {
        this.data = data;
    }

    @Override
    public Iterator<CharSequence> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < data.length();
            }

            @Override
            public CharSequence next() {
                CharSequence result = new Suffix(index);
                index += 1;
                return result;
            }
        };
    }

    @Override
    public int size() {
        return data.length() - 1;
    }

    /**
     * A suffix of the data starting from the given begin index.
     */
    private class Suffix implements CharSequence {
        private final int offset;

        /**
         * Constructs a new suffix of the data from the given offset.
         *
         * @param begin index into data representing the start (inclusive).
         */
        Suffix(int begin) {
            if (begin < 0 || begin > data.length()) {
                throw new IndexOutOfBoundsException(begin);
            }
            this.offset = begin;
        }

        @Override
        public char charAt(int index) {
            return data.charAt(offset + index);
        }

        @Override
        public int length() {
            return data.length() - offset;
        }

        @Override
        public CharSequence subSequence(int begin, int end) {
            if (begin < 0 || begin > end || end > length()) {
                throw new IndexOutOfBoundsException(
                        "begin " + begin + ", end " + end + ", length " + length()
                );
            } else if (offset + end == data.length()) {
                return new Suffix(offset + begin);
            } else {
                return new SubSequence(offset + begin, offset + end);
            }
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder(length());
            for (int i = 0; i < length(); i += 1) {
                result.append(charAt(i));
            }
            return result.toString();
        }
    }

    /**
     * A subsequence of the data between the given begin and end indices.
     */
    private class SubSequence extends Suffix {
        private final int length;

        /**
         * Constructs a new subsequence of the data between the given begin and end indices.
         *
         * @param begin index into data representing the start (inclusive).
         * @param end   index into data representing the end (exclusive).
         */
        SubSequence(int begin, int end) {
            super(begin);
            if (begin < 0 || begin > end || end > data.length()) {
                throw new IndexOutOfBoundsException(
                        "begin " + begin + ", end " + end + ", length " + data.length()
                );
            }
            this.length = end - begin;
        }

        @Override
        public char charAt(int index) {
            if (index >= length) {
                throw new IndexOutOfBoundsException("index " + index + ", length " + length);
            }
            return super.charAt(index);
        }

        @Override
        public int length() {
            return length;
        }
    }
}
