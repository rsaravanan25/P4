import java.util.*;

public class HashListAutocomplete implements Autocompletor {

    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap;
    private int mySize;

    public HashListAutocomplete(String[] terms, double[] weights) {
    
        if (terms == null || weights == null) {
			throw new NullPointerException("One or more arguments null");
		}

        if (terms.length != weights.length) {
			throw new IllegalArgumentException("terms and weights are not the same length");
		}
		mySize = 0;
        initialize(terms,weights);
    }

    @Override
    public List<Term> topMatches(String prefix, int k) {
        if(k < 0) throw new IllegalArgumentException();
        String nPrefix = prefix;
        if(prefix.isEmpty()) prefix = "";

        if(!(nPrefix.length() <= MAX_PREFIX)) nPrefix = prefix.substring(0, MAX_PREFIX); 

        if(myMap.containsKey(nPrefix)) {
            List<Term> all = myMap.get(nPrefix);
            List<Term> list = all.subList(0, Math.min(k, all.size()));
        return list;
    }
        return new ArrayList<>();
    }

    @Override
    public void initialize(String[] terms, double[] weights) {
        // TODO Auto-generated method stub
        if (terms.length != weights.length){
            throw new IllegalArgumentException();
        }

        HashMap<String, List<Term>> lib = new HashMap<>();


        for(int i = 0; i < terms.length; i++)
        {
            String word = terms[i];

            for(int k = 0; k <= Math.min(word.length(), MAX_PREFIX); k++)
            {
                String sub = word.substring(0, k);
                Term trm = new Term(word, weights[i]);
                mySize+= BYTES_PER_DOUBLE + BYTES_PER_CHAR*trm.getWord().length();

                if(!lib.containsKey(sub)) mySize+=BYTES_PER_CHAR*sub.length();

                lib.putIfAbsent(sub, new ArrayList<>());
                
                lib.get(sub).add(trm);
            }
        }


        for(Map.Entry<String, List<Term>> entry: lib.entrySet()){
            Collections.sort(entry.getValue(), Comparator.comparing(Term::getWeight).reversed());
        }

        myMap = lib;

    }

    @Override
    public int sizeInBytes() {
        // TODO Auto-generated method stub
        return mySize;
    }
    
}

