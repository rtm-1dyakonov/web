import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RentalClient
{
    String localhost    = "127.0.0.1";
    String RMI_HOSTNAME = "java.rmi.server.hostname";
    String SERVICE_PATH = "rmi://localhost/RentalService";

    private void writeError(String text)
    {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
            Date currentDate = new Date();
            FileWriter writer = new FileWriter("error.txt", true);
            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.newLine();
            bufferWriter.write(formatter.format(currentDate).toString()); // дата ошибки
            bufferWriter.newLine();
            bufferWriter.write(text); // текст ошибки
            bufferWriter.close();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    public RentalClient(String inputFilename, String outputFilename) throws IOException {
        try {

            System.setProperty(RMI_HOSTNAME, localhost);
            // URL удаленного объекта
            String objectName = SERVICE_PATH;
            RentalService bs;
            bs = (RentalService) Naming.lookup(objectName);

            Rental rental = new Rental("st. Kuznecova, 22, Samara", 100); //вручную добавлены дубликаты чтобы было видно работу сервера по их удалению
            rental.add(new Car("Z", "Porshe", 1200000, 1));
            rental.add(new Car("A9","Audi",390000,10));
            rental.add(new Car("Z","Porshe",1200000,1));
            rental.add(new Car("Clarenta","Devo",90000,2));
            rental.read(inputFilename); // чтение из файла

            System.out.println("Исходные данные");
            rental.print();
            rental = bs.handleRental(rental);
            rental.write(outputFilename); // запись в файл
            System.out.println("Обработанные данные");
            rental.print();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            writeError(e.getMessage());
        }
          catch (RemoteException e) {
            e.printStackTrace();
              writeError(e.getMessage());
          } catch (NotBoundException e) {
            e.printStackTrace();
            writeError(e.getMessage());
         } catch (Exception e) {
            e.printStackTrace();
            writeError(e.getMessage());
            }
    }

    public static void main(String[] args) throws IOException {
        new RentalClient(args[0], args[1]);
        System.exit(0);
    }
}
