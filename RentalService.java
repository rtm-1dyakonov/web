import java.rmi.*;

public interface RentalService extends Remote
{
    // Обработка списка авто на точке проката
    public Rental handleRental(Rental rental) throws RemoteException;
}