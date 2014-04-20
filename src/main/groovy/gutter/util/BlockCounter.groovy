package gutter.util

class BlockCounter {
    private int count = 0

    void feed(String input) {
        count += input.count("{")
    }

    int getCount() {
        count
    }
}
