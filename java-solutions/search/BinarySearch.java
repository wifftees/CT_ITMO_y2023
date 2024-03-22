package search;

public class BinarySearch {
    public static void main(String[] args) {
        // P: Integer.parseInt(arg[0]) = args[0] in Integer type
        int value = Integer.parseInt(args[0]);
        // Q: value =  args[0] in Integer type

        // P: new int[args.length - 1] = [0, 0 ... 0]
        //                          && new int[args.length - 1].length =  args.length - 1
        int[] arr = new int[args.length - 1];
        // Q: arr = [0, 0 ... , 0] && arr.length = args.length - 1

        // P: true
        int i = 1;
        // Q: i == 1

        // P:  invariant: ∀ j ∈ [1: i'): arr[j - 1] == args[j] in Integer value
        while (i < args.length) {
            // P: Integer.parseInt(args[i]) == args[i] in Integer type && invariant
            arr[i - 1] = Integer.parseInt(args[i]);
            // Q: arr[i - 1] = args[i] in Integer type && invariant

            // P: ∀ j ∈ [1: i']: arr[j-1] == args[j] in Integer value
            i++;
            // Q: invariant: ∀ j ∈ [1: i' + 1): arr[j - 1] == args[j] in Integer value
        }
        // Q: invariant && i >= args.length



        int sum = findSum(arr);
        int result;
        if (sum % 2 == 0) {
            /*
                P:
                   value ∈ R &&
                   arr.length >= 0 &&
                   right >= 0 &&
                   left >= right &&
                   ∀ i: i >= right: arr[i] >= value &&
                   ∀ i: i < left: arr[i] < value &&
                   a[0] <= a[1] <= ... a[n-1] <= a[n]
             */
            result = recursiveBinarySearch(value, arr, 0, arr.length);
            // Q: result = R
        } else {
            // P: value ∈ R && arr.length >= 0 && a[0] <= a[1] <= ... a[n-1] <= a[n]
            result = iterativeBinarySearch(value, arr);
            // Q: result = R
        }

        System.out.println(result);
    }

    /*
        P: arr[0] + arr[1] + ... + arr[arr.length - 1] <= Integer.MAX_VALUE
     */
    private static int findSum(int[] arr) {
        int sum = 0;
        for (int j : arr) {
            sum += j;
        }

        return sum;
    }
    /*
        Q:
            if (arr.length != 0) {
                R = arr[0] + ... + arr[arr.length-1]
            } else {
                R = 0
            }
     */


    /*
        План док-ва:
            - Доказываю три инварианта
            - Из них делаю вывод, что answer соответствует контракту
     */

    // P: value ∈ R && arr.length >= 0 && a[0] <= a[1] <= ... a[n-1] <= a[n]

    public static int iterativeBinarySearch(int value, int[] arr) {
        // P: true
        int left = 0;
        // Q: left = 0

        // P: true
        int right = arr.length;
        // Q: right = arr.length

        // P: true
        int answer = arr.length;
        // Q: answer = arr.length

        /*
            P: invariant_1 {
                    answer' == arr.length || arr[answer'] <= value
               }

               invariant_2 {
                    invariant_2.1: ∀ i: i >= right': arr[i] <= value
                    invariant_2.2: ∀ i: i < left': arr[i] > value
               }
         */

        while (left != right) {
            int middle = left + (right - left) / 2;
            /*
                  if ((right' - left') % 2 == 0) -> middle' = left' + (right' - left') / 2
                  if ((right - left) % 2 == 1) -> middle' = left' + (right' - left' - 1) / 2
             */

            if (arr[middle] <= value) {
                /*
                        invariant: answer' == arr.length || arr[answer'] <= value
                        &&
                        if_cond: arr[middle'] <= value
                */
                answer = middle;
                // answer' == arr.length || arr[answer'] <= value



                /*
                    if_cond: arr[middle'] <= value
                    &&
                    invariant_2.1: ∀ i: i >= middle': arr[i] <= value
                    (because value >= arr[middle'] >= arr[middle' + 1] >= ...)
                 */
                right = middle;
                // invariant_2.1: ∀ i: i >= right': arr[i] <= value
            } else {
                /*
                    !if_cond: arr[middle'] > value
                    &&
                    invariant_2.2: ∀ i: i < middle' + 1: arr[i] > value
                    (because ... >= arr[middle' - 1] >= arr[middle'] > value)
                 */
                left = middle + 1;
                // invariant_2.2: ∀ i: i < left': arr[i] > value
            }
        }

        /*
            invariant_2.1: ∀ i: i >= right': arr[i] <= value
            invariant_2.2: ∀ i: i < left': arr[i] > value
            !while_cond: left' == right'

            !while_cond -> answer' == right' == left' (check code) -> {
                    invariant_2.1 -> arr[answer'] <= value
                    &&
                    invariant_2.2 -> (answer' == 0 || arr[answer' - 1] > value)
            }
         */



        return answer;
    }
    /*
        Q:
            if (∃ i: arr[i] <= value) -> {
                arr[R] <= value
                &&
                (R == 0 || arr[R - 1] > value)
            }
            else -> R == arr.length
     */





    /*
        План док-ва:
            - Доказываем, что будем уходить в рекурсию, пока left` != `right
            - Используем Q1 при left'==right'
     */

    /*
        P:
           value ∈ R &&
           arr.length >= 0 &&
           right >= 0 &&
           left >= right &&
           ∀ i: i >= right: arr[i] >= value &&
           ∀ i: i < left: arr[i] < value &&
           a[0] <= a[1] <= ... a[n-1] <= a[n]
     */
    public static int recursiveBinarySearch(int value, int[] arr, int left, int right) {
        /*
           Initial_P1: ∀ i: i >= right': arr[i] >= value &&
           Initial_P2: ∀ i: i < left': arr[i] < value
         */

        if (left == right && arr.length != 0 && left < arr.length) {
            /*
                P1.1: left' == right' && left' < arr.length && arr.length > 0
             */
            return left;
            /*
                Короткая версия
                Q1.1 = {
                     left'==right'
                     R ∈ [left'; right']
                     &&
                     arr[R] <= value
                     &&
                     (R == left' || arr[R - 1] > value)
                }

                Версия с пруфом.
                Q1.1: {
                    R == left' == right' && R < arr.length =>
                                     R ∈ [left'; right']
                    &&
                    (
                        left'==right'
                        &&
                        P1: ∀ i: i >= right': arr[i] >= value &&
                        &&
                        P2: ∀ i: i < left': arr[i] < value
                    ) => {
                        arr[R] <= value
                        &&
                        (R == left' || arr[R - 1] > value)
                    }
                }
             */
        } else if (left == right) {
            /*
                P1.2: left' == right' && (arr.length == 0 || left >= arr.length)
             */
            return arr.length;
            /*
                Q1.2: left == right && R = arr.length

                Доказываю, почему контракт выполняется
                if (arr.length == 0) -> ∄ i ∈ [left, right] => ∄ i ∈ [left, right]: value >= a[i]

                if (left >= arr.length && P2: ∀ i: i < left': arr[i] < value) =>
                                            ->  ∄ i ∈ [left, right]: value >= a[i]
             */
        }

        /*
            (Q1.1 && Q1.2) => Q1: {
                left'==right' !!! Только при этом условии, контракт выполняется
                &&
                if (∃ i ∈ [left; right]: arr[i] <= value) -> {
                    R ∈ [left; right]
                    &&
                    arr[R] <= value
                    &&
                    (R == left || arr[R - 1] > value)
                }
                else -> R == arr.length
         */


        /*
            (
                !Q1
                &&
                left' <= right' (по контракту функции)
            ) => left' < right' =>
                         left' + (right' - left') / 2 < right'
         */
        int middle = left + (right - left) / 2;
        /*
            Q: middle < right' => [left'; middle'] ⊂ [left'; right']
                                  [middle' + 1; right'] ⊂ [left'; right']

            Отрезок всегда уменьшается и по контракту left' <= right'
         */

        if (value >= arr[middle]) {
            /*
                middle' = left' + ε -> left' <= middle'
                if_cond: value >= arr[middle']
                New_P1: ∀ i: i >= middle': arr[i] >= value
                        (because value >= arr[middle'] >= arr[middle' + 1] >= ...)
                New_P2: Initial_P2
             */
            return recursiveBinarySearch(value, arr, left, middle);
            /*
                R - return value
                Q2:
                    if (∃ i ∈ [left'; middle'): arr[i] <= value) -> {
                        R ∈ [left'; middle']
                        &&
                        arr[R] <= value
                        &&
                        (R == left' || arr[R - 1] > value)
                    }
                    else -> R == arr.length
             */
        } else {
            /*
                middle' < right' && middle', right' ∈ ℤ => middle' + 1 <= right' <---
                !if_cond: value < arr[middle']
                New_P1: Initial_P1
                New_P2:
                    P2: ∀ i: i < middle' + 1: arr[i] < value
                    (because ... <= arr[middle' - 1] <= arr[middle'] > value)
             */
            return recursiveBinarySearch(value, arr, middle + 1, right);
            /*
                R - return value
                Q3:
                    if (∃ i ∈ [middle' + 1; right'); : arr[i] <= value) -> {
                        R ∈ [middle' + 1; right']
                        &&
                        arr[R] <= value
                        &&
                        (R == middle' + 1 || arr[R - 1] > value)
                    }
                    else -> R == arr.length
             */
        }

        /*
            (Q1 && Q2 && Q3) -> Final_Q: {

                R ∈ [left'; right']
                &&
                arr[R] <= value
                &&
                (R == left' || arr[R - 1] > value)
            }
         */
    }
    /*
        R - return value
        Q:
            if (∃ i ∈ [left; right]: arr[i] <= value) -> {
                R ∈ [left; right]
                &&
                arr[R] <= value
                &&
                (R == left || arr[R - 1] > value)
            }
            else -> R == arr.length

            Где ветка "else" значит тоже, что
                ∄ i ∈ [left, right]: value >= a[i]
     */
}
