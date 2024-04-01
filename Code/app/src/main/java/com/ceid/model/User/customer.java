public class customer extends User {

    private Wallet wallet;
    private Profil profil;


    customer(int id,String username,String name,String lname,String email,String info,Wallet wallet){
        super(id,username,name,lname,email);
        this.wallet=wallet;
        profil=new Profil(name,info);
    }



    public class Profil{
        private String name;
        private String info;

        private Profil(String name,String info){
            this.name=name;
            this.info=info;
        }
    }
      
}
