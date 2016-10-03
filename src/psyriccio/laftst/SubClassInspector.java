/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package psyriccio.laftst;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author psyriccio
 */
public class SubClassInspector {
    
    public static List<Class> inspect(Class clazz) {
        List<Class> res = new ArrayList<>();
                FastClasspathScanner flps = new FastClasspathScanner();
        ScanResult refl = flps.scan();
        refl.getNamesOfAllClasses().forEach((clz) -> {
            Class supClass = null;
            try {
                supClass = Class.forName(clz, false, ClassLoader.getSystemClassLoader());
                if(supClass != null) {
                    while(supClass != null && !supClass.equals(Object.class)) {
                        if(supClass.equals(clazz)) {
                            res.add(Class.forName(clz));
                        }
                        supClass = supClass.getSuperclass();
                    }
                }
            } catch (ClassNotFoundException | NoClassDefFoundError ex) {
            }
        });

        return res;
    }
    
}
