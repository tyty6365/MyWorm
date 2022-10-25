package MyWorm;

public class BiohazardException extends RuntimeException {
    public BiohazardException(String type) {
        super("Adding '" + type + "' to a worm is unnatural!");
    }
}
