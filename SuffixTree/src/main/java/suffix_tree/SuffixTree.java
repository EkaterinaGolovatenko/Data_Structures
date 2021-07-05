package main.java.suffix_tree;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SuffixTree {

  private static final String WORD_TERMINATION = "$";
  private static final int POSITION_UNDEFINED = -1;
  private String fullText;
  private ArrayList<Node> nodes;
  private Node root;

  public SuffixTree(String text) {
    root = new Node("", POSITION_UNDEFINED);
    for (int i = 0; i < text.length(); i++) {
      addSuffix(text.substring(i) + WORD_TERMINATION, i);
    }
    fullText = text;
  }

  //добавляем дочерний узел
  private void addChildNode(Node parentNode, String text, int index) {
    parentNode.getNextNodes().add(new Node(text, index));
  }

  //ищим самый длинный общий префикс у двух строк
  private String getLongestCommonPrefix(String str1, String str2) {
    int compareLength = Math.min(str1.length(), str2.length());
    for (int i = 0; i < compareLength; i++) {
      if (str1.charAt(i) != str2.charAt(i)) {
        return str1.substring(0, i);
      }
    }
    return str1.substring(0, compareLength);
  }

  /*разделение узла на ребенка и родителя. Метод используется в другом методе, extendNode, когда строка
  уже разделена
  */
  private void splitNodeToParentAndChild(Node parentNode, String parentNewText,
      String childNewText) {
    Node childNode = new Node(childNewText, parentNode.getPosition());

    if (parentNode.getNextNodes().size() > 0) {
      while (parentNode.getNextNodes().size() > 0) {
        childNode.getNextNodes()
            .add(parentNode.getNextNodes().remove(0));
      }
    }

    parentNode.getNextNodes().add(childNode);
    parentNode.setFragment(parentNewText);
    parentNode.setPosition(POSITION_UNDEFINED);
  }

  /*Получаем все узлы. Метод используется для создания дерева и поиска подстроки
  * Частичное совпадение: при добавлении нового узда смотрим, есть ли узел, который можно изменить или
  * расширить. Первый символ совпадает: берем узел и делим его.
  * Для поиска подстроки нужно полное совпадение.
  * boolean isAllowPartialMatch используется для указания на то, создаем мы дерево или ищем подстроку*/
  private List<Node> getAllNodesInTraversePath(String pattern, Node startNode,
      boolean isAllowPartialMatch) {
    List<Node> nodes = new ArrayList<>();
    for (int i = 0; i < startNode.getNextNodes().size(); i++) {
      Node currentNode = startNode.getNextNodes().get(i);
      String nodeText = currentNode.getFragment();
      if (pattern.charAt(0) == nodeText.charAt(0)) { //сравниваем первый символ подстроки и узла
        if (isAllowPartialMatch && pattern.length() <= nodeText.length()) { /*если создаем дерево и подстрока короче или равна по длине узлу,
        добавляем узел в список останавливаемся на этом */
          nodes.add(currentNode);
          return nodes;
        }

        int compareLength = Math.min(nodeText.length(), pattern.length()); //сравниваем оставшиеся символы узла и подстроки
        for (int j = 1; j < compareLength; j++) {
          if (pattern.charAt(j) != nodeText.charAt(j)) {
            if (isAllowPartialMatch) {
              nodes.add(currentNode); //при несовпадении останавливаемся и добавляем в список только если разрешено частичное совпадение
            }
            return nodes;
          }
        }

        nodes.add(currentNode); // если строка совпадает полностью, добавляем в список узлов
        if (pattern.length() > compareLength) { // если в подстроке больше символов, проверяем дочерние узлы
          List<Node> nodes2 = getAllNodesInTraversePath(pattern.substring(compareLength), //проверяем все узлы рекурсивно
              currentNode, isAllowPartialMatch);
          if (nodes2.size() > 0) {
            nodes.addAll(nodes2); //если есть совпадения, они добавляются в список
          } else if (!isAllowPartialMatch) {
            nodes.add(null); //если нужно полное совпадение, а список пуст, значит, где-то было несовпадение, добавляем null элемент
          }
        }
        return nodes;
      }
    }
    return nodes;
  }
/*
Для изменения существующего узла. Разбиваем существующий узел на родительский и дочерний.
Получаем текстовый фрагмент родителя, определяем самый длинный общий префикс у нового фрагмента и родителя,
разбиваем узел, затем вызываем метод разделения узла
с использованием полученных фрагментов текста. Добавляем ребенка. Родительский узел разбивается для того, чтобы сделать его общим
для всех детей
*/
  private void extendNode(Node node, String newText, int position) {
    String currentText = node.getFragment();
    String commonPrefix = getLongestCommonPrefix(currentText, newText);
    if (!commonPrefix.equals(currentText)) {
      String parentText = currentText.substring(0, commonPrefix.length());
      String childText = currentText.substring(commonPrefix.length());
      splitNodeToParentAndChild(node, parentText, childText);
    }

    String remainingText = newText.substring(commonPrefix.length());
    addChildNode(node, remainingText, position);
  }
//добавляем суффикс
  private void addSuffix(String suffix, int position) {
    List<Node> nodes = getAllNodesInTraversePath(suffix, root, true); //проверяем, существует ли путь
    if (nodes.size() == 0) {
      addChildNode(root, suffix, position); //добавляем суффикс в качестве дочернего узла, если пути нет
    } else { //если путь существует, надо изменить существующий узел. Этот узел будет последним в списке
      Node lastNode = nodes.remove(nodes.size() - 1);
      String newText = suffix;
      if (nodes.size() > 0) { //если в списке больше одного элемента, исключаем общий префикс из суффикса до послднего узла и получаем новый текст для узла
        String existingSuffixUptoLastNode = nodes.stream()
            .map(a -> a.getFragment())
            .reduce("", String::concat);
        newText = newText.substring(existingSuffixUptoLastNode.length());
      }
      extendNode(lastNode, newText, position); //меняем узел
    }
  }
//поиск текста
  public List<String> searchText(String pattern) {
    List<String> result = new ArrayList<>();
    List<Node> nodes = getAllNodesInTraversePath(pattern, root, false); //проверяем, есть ли подстрока в дереве
    if (nodes.size() > 0) {
      Node lastNode = nodes.get(nodes.size() - 1); //последний узел в списке - тот, до которого подстрока полностью совпадает
      if (lastNode != null) {
        List<Integer> positions = searchPositions(lastNode); //получаем все листы от последнего узла (позиции)
        positions = positions.stream()
            .sorted()
            .collect(Collectors.toList());
        positions.forEach(m -> result.add((markPatternInText(m, pattern)))); //по полученным позициям выделяем совпадения в тексте
      }
    }
    return result;
  }
//отмечаем место в тексте, где нашли совпадение. startPosition - начало суффикса, длина подстроки - количество символов
  private String markPatternInText(Integer startPosition, String pattern) {
    String matchingTextLHS = fullText.substring(0, startPosition);
    String matchingText = fullText.substring(startPosition, startPosition + pattern.length());
    String matchingTextRHS = fullText.substring(startPosition + pattern.length());
    return matchingTextLHS + "[" + matchingText + "]" + matchingTextRHS; //выделяем отрезок совпадения скобками
  }
//получаем позиции листов узла
  private List<Integer> searchPositions(Node node) {
    ArrayList<Integer> positions = new ArrayList<>();
    if (node.getFragment().endsWith(WORD_TERMINATION)) { //проверяем, содержит ли узел конечную часть суффикса и добавляем
      positions.add(node.getPosition());
    }
    for (int i = 0; i < node.getNextNodes().size(); i++) { //то же самое нужно проделать рекурсивно для каждого ребенка узла
      positions.addAll(searchPositions(node.getNextNodes().get(i)));
    }
    return positions;
  }
}