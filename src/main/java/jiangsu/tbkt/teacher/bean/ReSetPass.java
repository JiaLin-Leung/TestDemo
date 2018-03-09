package jiangsu.tbkt.teacher.bean;


/**
 *
 * @Description:  重新设置密码返回数据封装类
 * @FileName: ReSetPass.java
 * @Package com.tbkt.teacher_eng.javabean.register
 * @Author wangxiao
 * @Date 2015-4-14
 * @Version V1.0
 */
public class ReSetPass {

    private ResultBean  resultBean;

    private String newpassword="";


    @Override
    public String toString() {
        return "ReSetPass{" +
            "newpassword='" + newpassword + '\'' +
            '}';
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(ResultBean resultBean) {
        this.resultBean = resultBean;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }
}
