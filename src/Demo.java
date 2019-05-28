import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo {
    @Test
    public void fun1(){
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String s=sdf.format(d);
        System.out.println(s);
    }
}
