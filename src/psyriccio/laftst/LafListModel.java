/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psyriccio.laftst;

import ch.qos.logback.classic.Logger;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ListDataListener;
import org.slf4j.LoggerFactory;

/**
 *
 * @author psyriccio
 */
public class LafListModel implements ListModel<LafListModel.Item> {

    public class Item {
        
        private String name;
        private String fullName;

        public Item(String name, String fullName) {
            this.name = name;
            this.fullName = fullName;
        }
        
        public String getName() {
            return name;
        }

        public String getFullName() {
            return fullName;
        }

        @Override
        public String toString() {
            return "<html><b>" + name + "</b> <font size=-1>(" + fullName + ")</font></html>";
        }
    
    }
    
    
    private static final Logger log = (Logger) LoggerFactory.getLogger(LafListModel.class);
    
    final private List<Item> items;
    final private List<ListDataListener> listeners;

    private static void loadJar(File file) throws Exception {
        Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
        method.setAccessible(true);
        method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
    }

    public LafListModel() {
        List<URL> urls = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.items = new ArrayList<>();
        for (File fl : new File("lib/").listFiles()) {
            try {
                log.info("Loading {}", fl.getAbsolutePath());
                loadJar(fl);
                urls.add(fl.toURI().toURL());
            } catch (Exception ex) {
                log.error("Ex: {}", ex);
            }
        }
        for (UIManager.LookAndFeelInfo ilf : UIManager.getInstalledLookAndFeels()) {
            this.items.add(new Item(ilf.getName().replaceFirst("^.+\\.", ""), ilf.getClassName()));
        }

        SubClassInspector.inspect(LookAndFeel.class).forEach((clz) -> {
            log.info("Finded LaF {}", clz);
            if(!this.items.contains(clz.getCanonicalName())) {
                this.items.add(new Item(clz.getSimpleName(), clz.getCanonicalName()));
            }
        });
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public Item getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

}
