package xyz.vsl.tomcat.secdb;

/**
* @author Vladimir Lokhov
*/
class Either<T> {
    private T value;
    private Exception error;

    Either(T value) {
        this.value = value;
    }

    Either(Exception error) {
        this.error = error;
    }

    public T getValue() {
        return value;
    }

    public Exception getError() {
        return error;
    }
}
