import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RentalServiceImpl extends UnicastRemoteObject
        implements RentalService
{
    private static final long serialVersionUID = 1L;

    // инициализация сервера
    public RentalServiceImpl() throws RemoteException
    {
        super();
    }

    @Override
    public Rental handleRental(Rental rental)
    {
        rental.deleteDuplicates();
        rental.sort();
        return rental;
    }

    /**
     * Старт удаленного RMI объекта RentalService
     * @param args аргументы
     * @throws Exception
     */
    public static void main (String[] args) throws Exception
    {
        String localhost    = "127.0.0.1";
        String RMI_HOSTNAME = "java.rmi.server.hostname";
        try {
            System.setProperty(RMI_HOSTNAME, localhost);
            // Создание удаленного RMI объекта
            RentalService service = new RentalServiceImpl();

            // Определение имени удаленного RMI объекта
            String serviceName = "RentalService";

            System.out.println("Инициализация " + serviceName);

            /*
             * Регистрация удаленного RMI объекта RentalService
             * в реестре rmiregistry
             */
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(serviceName, service);
            System.out.println("Старт " + serviceName);
        } catch (RemoteException e) {
            System.err.println("RemoteException : "+e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Exception : " + e.getMessage());
            System.exit(2);
        }
    }
}