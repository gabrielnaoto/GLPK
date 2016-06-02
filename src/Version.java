
import org.gnu.glpk.GLPK;

public class Version {

    public static void main(String[] args) {
        System.out.println(GLPK.glp_version());
    }
}
