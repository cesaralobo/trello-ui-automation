package framework.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class CodeEntityGen {

    private List<Integer> numbers;
    private Iterator<Integer> it;
    private static CodeEntityGen instance;

    private CodeEntityGen(int qty) {

        int capacity = qty;
        this.numbers = new ArrayList<>(capacity);

        for (int i = 0; i < capacity; i++) {
            this.numbers.add(i);
        }
        Collections.shuffle(this.numbers);
        it = this.getCodeList().iterator();

    }

    public static CodeEntityGen getInstance(int qty) {
        instance = new CodeEntityGen(qty);
        return instance;
    }

    public List<Integer> getCodeList(){
        return this.numbers;
    }

    public String getNextCode(String prefix){
        return prefix+it.next();
    }

}
