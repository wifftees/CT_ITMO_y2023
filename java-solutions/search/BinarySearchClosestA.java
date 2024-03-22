package search;

public class BinarySearchClosestA {
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

        // P: invariant[arr.length == args.length]
        while (i < args.length) {
            // P: Integer.parseInt(args[i]) = args[i] in Integer type && invariant
            arr[i - 1] = Integer.parseInt(args[i]);
            // Q: arr[i - 1] = args[i] in Integer type && invariant

            // P: invariant
            i++;
            // Q: invariant
        }
        // Q: invariant && i >= args.length

        int result;
        if (findSum(arr) % 2 == 0) {
            /*
                a[0] <= a[1] <= a[2] ... <= a[n]
                &&
                invariant_1 {
                    right == arr.length
                    ||
                    |arr[right' - 1] - value| <= |arr[right'] - value|
                }
                &&
                invariant_2 {
                    left' == 0
                    ||
                    |arr[left' - 1] - value| >= |arr[left'] - value|
                }
                &&
                invariant_3 {
                    I = { |arr[i] - value| | arr[i] == arr[i+1]}
                    min(I) ∈ [left'; right')
                }
                &&
                left < right
                &&
                arr.length > 0;
                &&
                right > 0
             */
            result = recursiveClosestA(value, arr, 0, arr.length);
            // result = R
        } else {
            /*
                arr.length > 0
                &&
                a[0] <= a[1] <= a[2] ... <= a[n]
             */
            result = iterativeClosestA(value, arr);
            // result = R
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
        P: {
            arr.length > 0
            &&
            a[0] <= a[1] <= a[2] ... <= a[n]
        }
        Q: ∀ x ∈ arr: |value - R| <= |value - x|
     */
    public static int iterativeClosestA(int value, int[] arr) {
        int left = 0;
        int right = arr.length;

        /*
            invariant_1 {
                right == arr.length
                ||
                |arr[right' - 1] - value| <= |arr[right'] - value|
            }

            invariant_2 {
                left' == 0
                ||
                |arr[left' - 1] - value| >= |arr[left'] - value|
            }

            invariant_3 {
                I = { |arr[i] - value| | arr[i] == arr[i+1]}
                min(I) ∈ [left'; right')
            }
         */

        while (right - left != 1) {
            int middle = left + (right - left) / 2;

            int firstIndex = Math.max(middle - 1, left);

            long diff1 = Math.abs((long) arr[firstIndex] - value);
            long diff2 = Math.abs((long) arr[middle] - value);

            if (diff1 > diff2) {
                /*
                    diff1 > diff2
                    middle' == 0 (max(middle', left') = left')
                    ||
                    |arr[max(middle' - 1, left)] - value| > |arr[middle'] - value|
                 */
                left = middle;
                /*
                    left' == 0
                    ||
                    |arr[left' - 1] - value| >= |arr[left'] - value|

                    Так как массив отсортирован по не убыванию, то
                    ... >= |arr[left' - 2] >= |arr[left' - 1]| >= |arr[left'| - value|
                    ----> invariant_3 сохраняется

                 */
            } else if (diff1 < diff2) {
                /*
                    diff1 < diff2
                    middle == arr.length
                    ||
                    |arr[middle' - 1| - value|  < |arr[middle'] - value|
                 */
                right = middle;
                /*
                    right' == arr.length
                    ||
                    |arr[right' - 1| - value|  <= |arr[right'] - value| (invariant_1)

                    Так как массив отсортирован по не убыванию, то
                    |arr[right' - 1]| <= |arr[right'| - value| <= |arr[right' + 1] - value| <= ...
                    ----> invariant_3 сохраняется
                 */
            } else if (arr[middle] <= value) {
                /*
                    diff1 == diff2
                    &&
                    arr[middle'] < value
                    &&
                    |arr[middle' - 1| - value| == |arr[middle'] - value|
                 */
                left = middle;
                /*
                    (
                        left' == 0
                        ||
                        |arr[left' - 1] - value| >= |arr[left'] - value| (invariant_2)
                    )
                    &&
                    ∀ i <= left' - 1: arr[i] <= arr[left' -1] <= value --->
                    ---> |arr[i] - value| >= |arr[left' - 1] - value| --->
                    invariant_3 сохраняется
                 */
            } else {
                /*
                    diff1 == diff2
                    &&
                    arr[middle'] > value
                 */
                right = middle;
                /*
                    right' == arr.length
                    ||
                    |arr[right' - 1| - value| <= |arr[right'] - value| (invariant_1)

                    Так как массив отсортирован по не убыванию, то
                    ∀ i >= right': value < arr[right'] <= arr[i] --->
                    ---> |arr[right'] - value| <= |arr[i] - value| --->
                    invariant_3 сохраняется
                 */
            }
        }

        /*
            right == left + 1
            &&
            invariant_1 && invariant_2 && invariant_3

            invariant_1 -> {
                arr[left'] <= arr[right']
                &&
                |arr[left'] - value| <= |arr[right'] - value| ->
                -> ∀ i: i >= right': |arr[left'] - value| <= |arr[i] - value|
                (увеличивая arr[i] разница будет только увеличиваться)
            }

            invariant_2 -> {
                (Если left' == 0, то Q функции гарантируется первым инвариантом)

                |arr[left' - 1] - value| >= |arr[left'] - value| ->
                -> ∀ i: i <= left' - 1': |arr[i] - value| >= |arr[i] - value|
                (уменьшая arr[i] разница будет только увеличиваться)
            }

            invariant_3 -> {
                arr[left'] == arr[right'] --->
                -> ∀ x ∈ arr: |value - arr[left']| == |value - arr[left']| <= |value - x|

            }

            -> ∀ x ∈ arr: |value - arr[left]| <= |value - x|
         */

        return arr[left];
    }

    /*
        a[0] <= a[1] <= a[2] ... <= a[n]
        &&
        invariant_1 {
            right == arr.length
            ||
            |arr[right' - 1] - value| <= |arr[right'] - value|
        }
        &&
        invariant_2 {
            left' == 0
            ||
            |arr[left' - 1] - value| >= |arr[left'] - value|
        }
        &&
        invariant_3 {
            I = { |arr[i] - value| | arr[i] == arr[i+1]}
            min(I) ∈ [left'; right')
        }
        &&
        left < right
        &&
        arr.length > 0;
        &&
        right > 0
     */
    public static int recursiveClosestA(int value, int[] arr, int left, int right) {
        if (right - left == 1) {
            /*
                invariant_1 && invariant_2 && invariant_3 && (right - left) == 1 ->
                -> ∃! x ∈ arr[left; right)
                &&
                arr[left'] <= arr[right']
                &&
                |arr[left'] - value| <= |arr[right'] - value| ->
                -> ∀ i: i >= right': |arr[left'] - value| <= |arr[i] - value|
                (увеличивая arr[i] разница будет только увеличиваться)
                &&
                (Если left' == 0, то Q функции гарантируется первым инвариантом)

                |arr[left' - 1] - value| >= |arr[left'] - value| ->
                -> ∀ i: i <= left' - 1': |arr[i] - value| >= |arr[i] - value|
                (уменьшая arr[i] разница будет только увеличиваться)
                &&
                Если arr[left' - 1] == arr[left'] == arr[right'], из-за invariant_3 ->
                -> на arr[left; right) лежит наименьшее значение.
             */
            return arr[left];
        }

        int middle = left + (right - left) / 2;
        // Math.max(middle' - 1, left') = (middle' - 1 > left') ? middle' - 1 : left'
        int firstIndex = Math.max(middle - 1, left);
        // firstIndex' = (middle' - 1 > left') ? middle' - 1 : left';

        // Math.abs(arr[firstIndex'] - value) = |arr[firstIndex'] - value|
        long diff1 = Math.abs((long) arr[firstIndex] - value);
        // diff1 = |arr[firstIndex'] - value|
        // Math.abs(arr[middle'] - value) == |arr[middle'] - value|
        long diff2 = Math.abs((long) arr[middle] - value);
        // diff2 = |arr[middle'] - value|

        if (diff1 > diff2) {
            /*
                |arr[max(middle' - 1, left)] - value| > |arr[middle'] - value| -> invariant_2 (middle')
                &&
                ∀ i <= middle': arr[i] <= arr[middle']  --->
                ---> |arr[i] - value| >= |arr[middle'] - value| --->
                invariant_3 сохраняется
             */
            return recursiveClosestA(value, arr, middle, right);
        } else if (diff1 < diff2) {
            /*
                |arr[max(middle' - 1, left)] - value| < |arr[middle'] - value| -> invariant_1 (middle')
             */
            return recursiveClosestA(value, arr, left, middle);
        } else if (arr[middle] <= value) {
            /*
                |arr[max(middle' - 1, left)] - value| == |arr[middle'] - value|
                &&
                arr[middle'] <= value
                &&
                ∀ i <= middle': arr[i] <= arr[middle'] <= value --->
                ---> |arr[i] - value| >= |arr[middle'] - value| --->
                invariant_3 сохраняется
             */
            return recursiveClosestA(value, arr, middle, right);
        } else {
            /*
                |arr[max(middle' - 1, left)] - value| == |arr[middle'] - value|
                &&
                arr[middle'] > value
                &&
                Так как массив отсортирован по не убыванию, то
                ∀ i >= middle': value < arr[middle'] <= arr[i] --->
                ---> |arr[middle'] - value| <= |arr[i] - value| --->
                invariant_3 сохраняется
             */
            return recursiveClosestA(value, arr, left, middle);
        }
    }
    /*
        Q: ∀ x ∈ arr[left, right): |value - R| <= |value - x|
     */
}
