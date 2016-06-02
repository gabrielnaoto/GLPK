package teste;
import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.GlpkException;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;


public class Lp {
    public static void main(String[] arg) {
        glp_prob lp;
        glp_smcp parm;
        SWIGTYPE_p_int ind;
        SWIGTYPE_p_double val;
        int ret;

        try {
            // cria��o do problema
            lp = GLPK.glp_create_prob();
            System.out.println("Problema criado");
            GLPK.glp_set_prob_name(lp, "exemplo");

            // defini��o das colunas
            GLPK.glp_add_cols(lp, 3); //Quantidade de colunas
            GLPK.glp_set_col_name(lp, 1, "x1"); //nome
            GLPK.glp_set_col_kind(lp, 1, GLPKConstants.GLP_CV); //tipo
            GLPK.glp_set_col_bnds(lp, 1, GLPKConstants.GLP_LO, 0, 0); //limites
            GLPK.glp_set_col_name(lp, 2, "x2");
            GLPK.glp_set_col_kind(lp, 2, GLPKConstants.GLP_CV);
            GLPK.glp_set_col_bnds(lp, 2, GLPKConstants.GLP_LO, 0, 0);
            GLPK.glp_set_col_name(lp, 3, "x3");
            GLPK.glp_set_col_kind(lp, 3, GLPKConstants.GLP_CV);
            GLPK.glp_set_col_bnds(lp, 3, GLPKConstants.GLP_LO, 0, 0);

            // Criando restri��es

            // Alocando memoria
            ind = GLPK.new_intArray(3); //indice dos coeficientes
            val = GLPK.new_doubleArray(3); //coeficientes

            // Criando linhas
            GLPK.glp_add_rows(lp, 3);

            // setando coeficientes
            GLPK.glp_set_row_name(lp, 1, "r1");
            GLPK.glp_set_row_bnds(lp, 1, GLPKConstants.GLP_UP, 0, 100);
            GLPK.intArray_setitem(ind, 1, 1);
            GLPK.intArray_setitem(ind, 2, 2);
            GLPK.intArray_setitem(ind, 3, 3);
            GLPK.doubleArray_setitem(val, 1, 1);
            GLPK.doubleArray_setitem(val, 2, 1);
            GLPK.doubleArray_setitem(val, 3, 1);
            GLPK.glp_set_mat_row(lp, 1, 3, ind, val);

            GLPK.glp_set_row_name(lp, 2, "r2");
            GLPK.glp_set_row_bnds(lp, 2, GLPKConstants.GLP_UP, 0, 600);
            GLPK.intArray_setitem(ind, 1, 1);
            GLPK.intArray_setitem(ind, 2, 2);
            GLPK.intArray_setitem(ind, 3, 3);
            GLPK.doubleArray_setitem(val, 1, 10);
            GLPK.doubleArray_setitem(val, 2, 4);
            GLPK.doubleArray_setitem(val, 3, 5);
            GLPK.glp_set_mat_row(lp, 2, 3, ind, val);
            
            GLPK.glp_set_row_name(lp, 3, "r3");
            GLPK.glp_set_row_bnds(lp, 3, GLPKConstants.GLP_UP, 0, 300);
            GLPK.intArray_setitem(ind, 1, 1);
            GLPK.intArray_setitem(ind, 2, 2);
            GLPK.intArray_setitem(ind, 3, 3);
            GLPK.doubleArray_setitem(val, 1, 2);
            GLPK.doubleArray_setitem(val, 2, 2);
            GLPK.doubleArray_setitem(val, 3, 6);
            GLPK.glp_set_mat_row(lp, 3, 3, ind, val);

            // Liberando a memoria
            GLPK.delete_intArray(ind);
            GLPK.delete_doubleArray(val);

            // Definindo fun��o objetivo
            GLPK.glp_set_obj_name(lp, "z");
            GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
            GLPK.glp_set_obj_coef(lp, 1, 10);
            GLPK.glp_set_obj_coef(lp, 2, 6);
            GLPK.glp_set_obj_coef(lp, 3, 4);

            // Gravando Problema no arquivo
             GLPK.glp_write_lp(lp, null, "lpteste.lp");

            // resolvendo o problema
            parm = new glp_smcp();
            GLPK.glp_init_smcp(parm);
            ret = GLPK.glp_simplex(lp, parm);

            // retornando a solucao
            if (ret == 0) {
                write_lp_solution(lp);
            } else {
                System.out.println("O problema n�o pode ser resolvido");
            }

            // liberando memoria
            GLPK.glp_delete_prob(lp);
        } catch (GlpkException ex) {
            ex.printStackTrace();
	    ret = 1;
        }
	System.exit(ret);
    }

    /**
     * write simplex solution
     * @param lp problem
     */
    static void write_lp_solution(glp_prob lp) {
        int i;
        int n;
        String name;
        double val;

        name = GLPK.glp_get_obj_name(lp);
        val = GLPK.glp_get_obj_val(lp);
        System.out.print(name);
        System.out.print(" = ");
        System.out.println(val);
        n = GLPK.glp_get_num_cols(lp);
        for (i = 1; i <= n; i++) {
            name = GLPK.glp_get_col_name(lp, i);
            val = GLPK.glp_get_col_prim(lp, i);
            System.out.print(name);
            System.out.print(" = ");
            System.out.println(val);
        }
    }
}
