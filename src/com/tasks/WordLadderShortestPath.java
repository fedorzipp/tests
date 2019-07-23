package com.tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class WordLadderShortestPath {
    public static String startWord = "DAMP";
    public static String endWord = "LIKE";
    private static Set<String> dictionary = new HashSet<String>();

    public static void main(String[] args) {
        try {
            processInput(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> dictionary = new HashSet<String>();
        dictionary.add("DAMP");
        dictionary.add("LAMP");
        dictionary.add("LIMP");
        dictionary.add("LIME");
        dictionary.add("LIKE");

        Ladder result = getShortestTransformationIterative(startWord, endWord, dictionary);

        if (result!=null){
            System.out.println("Length is " + result.getLength() + " and path is : "+ String.join(" -> ", result.getPath()));
        } else{
            System.out.println("No Path Found");
        }

    }

    private static void processInput(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        System.out.println("Please insert start word: " );
        String startWordFromConsole = reader.readLine();
        if (startWordFromConsole != null && !startWordFromConsole.isEmpty()) startWord = startWordFromConsole;
        System.out.println("Start word is = " + startWord);

        System.out.println("Please insert end word: " );
        String endWordFromConsole = reader.readLine();
        if (endWordFromConsole != null && !endWordFromConsole.isEmpty()) endWord = endWordFromConsole;
        System.out.println("End word is = " + endWord);

        if (startWord.length() != endWord.length()) {
            System.out.println("Length of start and end words is not the same; Please check length of words. Length should be the same. Try again." );
            System.exit(0);
        }

        String line;
        System.out.println("Please insert dictionary words or leave it as default from program. And push 'END' to finish dictionary.");
        Set<String> dictionaryFromConsole = new HashSet<String>();
        while ((line = reader.readLine()) != null && !line.equals("END")) {
            if (line.length() != startWord.length()) System.out.println("This value has not added because length is not the same as start word. Please print word with correct length again.");
            else {
                dictionaryFromConsole.add(line);
            }
            System.out.println("Current dictionary is = " + dictionaryFromConsole.toString());
        }
        dictionary = dictionaryFromConsole;
        in.close();
    }

    private static Ladder getShortestTransformationIterative(String startWord, String endWord, Set<String> dictionary){
        if(dictionary.contains(startWord) && dictionary.contains(endWord)){

            List<String> path = new LinkedList<String>();
            path.add(startWord);

            //All intermediate paths are stored in queue.
            Queue<Ladder> queue = new LinkedList<Ladder>();
            queue.add(new Ladder(path, 1, startWord));

            //We took the startWord in consideration, So remove it from dictionary, otherwise we might pick it again.
            dictionary.remove(startWord);

            //Iterate till queue is not empty or endWord is found in Path.
            while(!queue.isEmpty() && !queue.peek().equals(endWord)){
                Ladder ladder = queue.remove();

                if(endWord.equals(ladder.getLastWord())){
                    return ladder;
                }

                Iterator<String> i = dictionary.iterator();
                while (i.hasNext()) {
                    String string = i.next();

                    if(differByOne(string, ladder.getLastWord())){

                        List<String> list = new LinkedList<String>(ladder.getPath());
                        list.add(string);

                        //If the words differ by one then dump it in Queue for later processsing.
                        queue.add(new Ladder(list, ladder.getLength() + 1, string));

                        //Once the word is picked in path, we don't need that word again, So remove it from dictionary.
                        i.remove();
                    }
                }
            }

            //Check is done to see, on what condition above loop break,
            //if break because of Queue is empty then we didn't got any path till endWord.
            //If break because of endWord matched, then we got the Path and return the path from head of Queue.
            if(!queue.isEmpty()){
                return queue.peek();
            }
        }
        return null;
    }

    private static Ladder findTransformationWay(String startWord, String endWord, Set<String> dictionary){

        //All Paths from startWord to endWord will be stored in "allPath"
        LinkedList<Ladder> allPath = new LinkedList<Ladder>();

        // Shortest path will be stored in "shortestPath"
        Ladder shortestPath = new Ladder(null);

        List<String> path = new LinkedList<String>();
        path.add(startWord);

        recursiveHelperShortest(startWord, endWord, dictionary, new Ladder(path, 1, startWord), allPath, shortestPath);

        return shortestPath;
    }

    private static void recursiveHelperShortest(String startWord, String endWord, Set<String> dictionary, Ladder ladder, LinkedList<Ladder> allPath, Ladder shortestPath){
        if(ladder.getLastWord().equals(endWord)){

            // For storing all paths
            allPath.add(new Ladder(new LinkedList<String>(ladder.getPath())));

            //For storing the shortest path from among all paths available
            if(shortestPath.getPath()==null || shortestPath.getPath().size()>ladder.getPath().size()){
                shortestPath.setPath(new LinkedList<String>(ladder.getPath()));
                shortestPath.setLength(ladder.getPath().size());
            }
            return;
        }

        Iterator<String> i = dictionary.iterator();
        while (i.hasNext()) {
            String string = i.next();

            if(differByOne(string, ladder.getLastWord()) && !ladder.getPath().contains(string)){

                List<String> path = ladder.getPath();
                path.add(string);

                //We found the new word in intermediate path, Start exploring new word from scratch again.
                recursiveHelperShortest(startWord, endWord, dictionary, new Ladder(path, ladder.getLength()+1, string), allPath, shortestPath);

                //After exploring new word, remove it from intermediate path.
                path.remove(path.size()-1);
            }
        }
    }

    private static boolean differByOne(String word1, String word2){
        if (word1.length() != word2.length()) {
            return false;
        }

        int diffCount = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                diffCount++;
            }
        }
        return (diffCount == 1);
    }

}