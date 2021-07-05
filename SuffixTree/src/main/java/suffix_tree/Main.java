package main.java.suffix_tree;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    SuffixTree suffixTree = new SuffixTree("cutelittlefrenchie");
    List<String> matches = suffixTree.searchText("e");
    matches.stream().forEach(m -> System.out.println(m));
  }

}
