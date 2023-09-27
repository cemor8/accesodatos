public class Dado {
    public Dado() {
    }
    public Integer tirada(){
        return (int) Math.floor(Math.random()*101);
    }
}
