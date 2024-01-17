package ru.geekbrains.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {


    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = ' ';
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;
    private static int fieldSizeX = 5;
    private static int fieldSizeY = 5;

    private static final int WIN_COUNT = 4; // Выигрышная комбинация


    public static void main(String[] args) {
        while (true){
            initialize();
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Инициализация объектов игры
     */
    static void initialize(){
        fieldSizeX = 5;
        fieldSizeY = 5;

        field = new char[fieldSizeX][fieldSizeY];

        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */
    static void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++){
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");


        for (int x = 0; x < fieldSizeX; x++){
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++){
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++){
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Ход игрока (человека)
     */
    static void humanTurn(){
        int x;
        int y;
        do{
            System.out.print("Введите координаты хода X и Y\n(от 1 до 3) через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAN;
    }

    /**
     * Ход игрока (компьютера)
     */
    static void aiTurn() {
        int x = -1;
        int y = -1;
        boolean flag = false;
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (isCellEmpty(i, j)) {
                    field[i][j] = DOT_HUMAN;
                    flag =  checkWin(DOT_HUMAN, WIN_COUNT);
                    if (flag) {
                        x = i;
                        y = j;
                        field[i][j] = DOT_EMPTY;
                        break;
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
            if (flag) break;
        }

        if ((x == -1) && (y == -1)) {
            do {
                x = random.nextInt(fieldSizeX);
                y = random.nextInt(fieldSizeY);
            }
            while (!isCellEmpty(x, y));
        }
        field[x][y] = DOT_AI;
    }

    /**
     * Проверка, является ли ячейка игрового поля пустой
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка валидности координат хода
     * @param x координата
     * @param y координата
     * @return результат проверки
     */
    static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Поверка на ничью (все ячейки игрового поля заполнены фишками человека или компьютера)
     * @return
     */
    static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++){
            for (int y = 0; y < fieldSizeY; y++){
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }

    static int checkRecurceWin(int x, int y, char dot,int side){
        switch (side){
            case 0:
                if (y+1 < fieldSizeY){
                    if (field[x][y+1] == dot) return checkRecurceWin(x,y+1,dot,0)+1;
                    else return 1;
                }
                return 1;
            case 1:
                if ((x+1 < fieldSizeX)&&(y+1 < fieldSizeY)){
                    if (field[x+1][y+1] == dot) return checkRecurceWin(x+1,y+1,dot,1)+1;
                    else return 1;
                }
                return 1;
            case 2:
                if ((x+1 < fieldSizeX)){
                    if (field[x+1][y] == dot) return checkRecurceWin(x+1,y,dot,2)+1;
                    else return 1;
                }
                return 1;
            case 3:
                if ((x+1 < fieldSizeX)&&(y-1 >= 0)){
                    if (field[x+1][y-1] == dot) return checkRecurceWin(x+1,y-1,dot,3)+1;
                    else return 1;
                }
                return 1;
            default:
                throw new RuntimeException("Wrong side");
        }
    }

    /**
     * TODO: Переработать в рамках домашней работы
     * Метод проверки победы
     * @param dot фишка игрока
     * @return результат проверки победы
     */
    static boolean checkWin(char dot, int win){
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                if (field[i][j] == dot){
                    if (checkRecurceWin(i,j,dot,0) == win) return true;
                    if (checkRecurceWin(i,j,dot,1) == win) return true;
                    if (checkRecurceWin(i,j,dot,2) == win) return true;
                    if (checkRecurceWin(i,j,dot,3) == win) return true;
                }
            }
        }
        return false;
    }

    static boolean checkWin(char dot){
        return checkWin(dot, WIN_COUNT);
    }

    static boolean check1(int x, int y, char dot, int win){
        return false;
    }

    static boolean check2(int x, int y, char dot, int win){
        return false;
    }

    static boolean check3(int x, int y, char dot, int win){
        return false;
    }

    static boolean check4(int x, int y, char dot, int win){
        return false;
    }

    /**
     * Проверка состояния игры
     * @param dot фишка игрока
     * @param s победный слоган
     * @return состояние игры
     */
    static boolean checkState(char dot, String s){
        if (checkWin(dot)){
            System.out.println(s);
            return true;
        }
        else if (checkDraw()){
            System.out.println("Ничья!");
            return true;
        }
        // Игра продолжается
        return false;
    }

}
