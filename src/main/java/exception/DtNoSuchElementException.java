package exception;

public class DtNoSuchElementException extends RuntimeException {

    private String locationElement;


    public DtNoSuchElementException(String locationElement)
    {
        this.locationElement = locationElement;
    }

    public String getLocationElement() {
        return locationElement;
    }

    public void setLocationElement(String locationElement) {
        this.locationElement = locationElement;
    }
}
