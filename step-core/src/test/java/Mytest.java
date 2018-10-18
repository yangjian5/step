import java.math.BigDecimal;

/**
 * Created by yangjian9 on 2018/10/17.
 */
public class Mytest {

    public static void main(String[] args){
        BigDecimal a = BigDecimal.valueOf(2).multiply(BigDecimal.valueOf(20.3));
        System.out.println(a.intValue());
    }
}
