package teste;

import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_iocp;

public class Mip {

    public static void main(String[] arg) {
        glp_prob lp;
        glp_iocp iocp;
        SWIGTYPE_p_int ind;
        SWIGTYPE_p_double val;
        int ret;

//  Cria��o do problema
        lp = GLPK.glp_create_prob();
        System.out.println("Problem created");
        GLPK.glp_set_prob_name(lp, "myProblem");

        // defini��o das colunas
        GLPK.glp_add_cols(lp, 3); //Quantidade de colunas
        GLPK.glp_set_col_name(lp, 1, "x1"); //nome
        GLPK.glp_set_col_kind(lp, 1, GLPKConstants.GLP_BV); //tipo
        GLPK.glp_set_col_name(lp, 2, "x2");
        GLPK.glp_set_col_kind(lp, 2, GLPKConstants.GLP_BV);
        GLPK.glp_set_col_name(lp, 3, "x3");
        GLPK.glp_set_col_kind(lp, 3, GLPKConstants.GLP_BV);

//  cria��o das restri��es
        ind = GLPK.new_intArray(3);
        val = GLPK.new_doubleArray(3);

        GLPK.glp_add_rows(lp, 3);
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

        GLPK.delete_doubleArray(val);
        GLPK.delete_intArray(ind);

//  definicao funcao objetivo
        GLPK.glp_set_obj_name(lp, "obj");
        GLPK.glp_set_obj_dir(lp, GLPKConstants.GLP_MAX);
        GLPK.glp_set_obj_coef(lp, 0, 0);
        GLPK.glp_set_obj_coef(lp, 1, 17);
        GLPK.glp_set_obj_coef(lp, 2, 12);
        GLPK.glp_set_obj_coef(lp, 3, 12);

//  resolvendo o problema
        iocp = new glp_iocp();
        GLPK.glp_init_iocp(iocp);
        iocp.setPresolve(GLPKConstants.GLP_ON);
        GLPK.glp_write_lp(lp, null, "mipteste.mip");
        ret = GLPK.glp_intopt(lp, iocp);

// retorno da solu��o
        if (ret == 0) {
            write_mip_solution(lp);
        } else {
            System.out.println("The problemcould not be solved");
        };

        // libera memoria
        GLPK.glp_delete_prob(lp);
    }

    /**
     * write integer solution
     *
     * @param mip problem
     */
    static void write_mip_solution(glp_prob lp) {
        int i;
        int n;
        String name;
        double val;

        name = GLPK.glp_get_obj_name(lp);
        val = GLPK.glp_mip_obj_val(lp);
        System.out.print(name);
        System.out.print(" = ");
        System.out.println(val);
        n = GLPK.glp_get_num_cols(lp);
        for (i = 1; i <= n; i++) {
            name = GLPK.glp_get_col_name(lp, i);
            val = GLPK.glp_mip_col_val(lp, i);
            System.out.print(name);
            System.out.print(" = ");
            System.out.println(val);
        }
    }
}
