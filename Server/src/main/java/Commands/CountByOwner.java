package Commands;

import DataClasses.Product;
import ProgramManager.CollectionManager;
import ProgramManager.Sender;
import ProgramManager.SerCommand;

import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class CountByOwner extends AbsCommand {
    private CollectionManager manager;

    public CountByOwner(CollectionManager manager){
        this.manager = manager;
    }

    /**
     * Метод считает количество элементов, значение owner которых совпадает с введенным
     *
     * @param commandPool
     * @param sendPool
     * @param key
     */
    @Override
    public void execute(SerCommand command, ExecutorService commandPool, ExecutorService sendPool, SelectionKey key) {
        Runnable countbyowner = () ->{
            if (!(manager.collection.size() == 0)) {
                //int count = 0;
                //for (Product p: manager.getCollection()){ if (p.getOwner().equals(person)){ count++; } }
                Stream<Product> stream = manager.collection.stream();
                long count = stream.filter(collection -> collection.getOwner().equals(command.getPerson())).count();
                sendPool.submit(new Sender(key, "Найдено " + count + " элемент(а/ов), значение поля owner которых совпадает с введенным"));
            } else {
                sendPool.submit(new Sender(key, "Коллекция пуста"));
            }
        };
        commandPool.execute(countbyowner);
    }
}
