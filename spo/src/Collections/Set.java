package Collections;
public class Set<KV> {//Key - Value

    private Node<KV>[] mass;
    int size;

    public Set(){
        mass = new Node[16];
    }

    public int getSize(){
        return size;
    }

    public void add(KV obj){
        System.out.println(obj.toString() + " - has added in set");

        Node buf = new Node(obj);
        //System.out.println(buf.getKey_value());
        if((mass.length + 1) * 3/4 <= size){
            size = 0;
            resize();
        }

        if(mass[index(buf)] != null){
            if(!contains(obj)){
                size++;
                mass[index(buf)].addOneMore(buf);
            }
        } else{
            mass[index(buf)] = buf;
            size++;

            //System.out.println("ind = " + index(buf) + " size  = " + size);
        }
    }

    public boolean contains(KV obj){
        Node buf = new Node(obj);

        if(mass[index(buf)] != null) {
            if (mass[index(buf)].equals(buf)) {
                return true;
            }
        }
        return false;
    }

    public boolean remove(KV obj){
        Node buf = new Node(obj);
        if(contains(obj)){
            System.out.println(obj + " - has been  deleted");
            if(buf.getKey_value().equals(mass[index(buf)].getKey_value()) && mass[index(buf)].getList() == null){
                mass[index(buf)] = null;
            } else if(buf.getKey_value().equals(mass[index(buf)].getKey_value()) && mass[index(buf)].getList() != null){
                mass[index(buf)].setKey_value(mass[index(buf)].getList().get(0).getKey_value());
                mass[index(buf)].getList().remove(0);
            } else {
                mass[index(buf)].getList().remove(mass[index(buf)].getList().contains(buf));
            }

            size--;
            return true;
        }
        return false;
    }

    private void resize(){
        Node<KV>[] buf = mass;
        mass = new Node[buf.length * 2];
        System.out.println("==========================");
        for (Node n : buf) {
            if(n != null){
                System.out.println(n.getKey_value());
                mass[index(n)] = n;
            }
        }
        System.out.println("==========================");
    }

    private int index(Node node){
        return node.getHash() % mass.length;
    }

    private class Node<KV>{
        private List<Node<KV>> nodes = null;
        private int hash;
        private KV key_value;
        private int size = 0;

        public Node(KV key_value) {
            this.key_value = key_value;
            this.hash = hashCode();
            if(hash < 0) hash = hash * (-1);
            size = 1;
        }

        public void addOneMore(Node newNode){
            if(nodes == null ){
                nodes = new List<>();
            }

            nodes.add(newNode);
            size++;
        }

        @Override
        public int hashCode() {
            hash = 31;
            hash = (hash * 17) + key_value.hashCode();
            return hash;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }

            if(obj instanceof Node){
                Node<KV> node = (Node) obj;
                if(size > 1) {
                    if(key_value.equals(node.getKey_value()) || nodes.contains(node) != -1) return true;
                } else {//System.out.println("obj = " + node.getKey_value());
                    if(key_value.equals(node.getKey_value())) return true;
                }
            }

            return false;
        }

        @Override
        public String toString(){
            String s = "value = " + key_value;
            if(nodes != null)  {
                s = s + " List : " + nodes.toString();
            }
            return s;
        }

        public void setKey_value(KV key_value) {
            this.key_value = key_value;
        }

        public int getHash() {
            //System.out.println("Value : " + key_value + " hash : " + hash);
            return hash;
        }

        public KV getKey_value() {
            return key_value;
        }

        public List<Node<KV>> getList() {
            return nodes;
        }
    }

    private String printSet(){
        String s = "{ ";
        for(Node n : mass){

            if(n != null){
                s = s + n.getKey_value()+ " ";
                if(n.getList() != null ){
                   s = s + " " + n.getList().toString() + " ";
                }
            }
        }
        s = s + "}";
        return s;
    }

    @Override
    public String toString(){
        return printSet();
    }
}