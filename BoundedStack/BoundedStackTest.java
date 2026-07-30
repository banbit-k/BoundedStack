import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoundedStackTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[PASS] " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea BoundedStackTest\n");
        }

        System.out.println("=== BoundedStack Test Suite ===\n");

        testCreators();
        testAdd();
        testObservers();
        testProducer();
        testExposure();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // --- Partition: ว่าง, มีสมาชิก, input ผิดเงื่อนไข ---
    private static void testCreators() {
        System.out.println("-- Creators --");

        // เช็คว่าว่างหรือไม่
        BoundedStack empty = new BoundedStack();   
        check("license() -> empty", empty.size() == 0);
        check("license() -> contains nothing", !empty.contains("anything"));
        
        // boundary: สร้างจาก list ที่มีสมาชิกไม่ซ้ำและไม่เกิน capacity และ ตัวอักษรไม่เกิน 8 ตัวอักษร และเรียงลำดับ
        BoundedStack box = new BoundedStack(Arrays.asList("ABCD1234", "BADC2341", "CBDA341"));
        check("new(list) -> size 3", box.size() == 3);
        check("new(list) -> contains BADC2341", box.contains("BADC2341"));
        check("new(list) -> preserves order",
                box.license().equals(Arrays.asList("ABCD1234", "BADC2341", "CBDA341")));
        check("every license has length <= 8", box.license().stream().allMatch(s -> s.length() <= 8));

        BoundedStack fromEmpty = new BoundedStack(new ArrayList<>());
        check("new(empty license) -> empty", fromEmpty.size() == 0);

        boolean threwDup = false;
        try {
            new BoundedStack(Arrays.asList("A", "A"));
        } catch (IllegalArgumentException e) {
            threwDup = true;
        }
        check("new(duplicates license) -> throws IllegalArgumentException", threwDup);
        
        boolean threwNull = false;
        try {
            new BoundedStack(Arrays.asList("A", null));
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("new(list with null) -> throws IllegalArgumentException", threwNull);
        
        boolean threwNullList = false;
        try {
            new BoundedStack(null);
        } catch (IllegalArgumentException e) {
            threwNullList = true;
        }
        check("new(null) -> throws IllegalArgumentException", threwNullList);
    }

    // --- Mutator:  ---
    private static void testAdd() {
        System.out.println("\n-- Mutator --");
        System.out.println(" --- Add ---");

        BoundedStack license = new BoundedStack();
        check("push(license) -> returns true", license.push("ABCD1234"));
        check("push(license) -> size 1", license.size() == 1);
        check("push(license) <= 8 characters", license.push("BADC2341") == true);
        check("push(license) -> found by contains", license.contains("ABCD1234"));

        license.push("CBDA341");
        check("push preserves insertion order",
                license.license().equals(Arrays.asList("ABCD1234", "BADC2341", "CBDA341")));     

        // เลขทะเบียนซ้ำไม่ใช่ error — คืน false เฉย ๆ
        check("add duplicate -> returns false", !license.push("ABCD1234"));
        check("failed add leaves size unchanged", license.size() == 3);

        // input ที่ผิดเงื่อนไขต้องโยน exception
        boolean threwEmpty = false;
        try {
            license.push("");
        } catch (IllegalArgumentException e) {
            threwEmpty = true;
        }
        check("add(empty string) -> throws IllegalArgumentException", threwEmpty);

        boolean threwNull = false;
        try {
            license.push(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        check("failed adds leave license unchanged", license.size() == 3);

        // boundary: เติมจนเต็มพอดีแล้วเติมเพิ่ม
        BoundedStack full = new BoundedStack();
        for (int i = 0; i < BoundedStack.capacity; i++) {
            full.push("List" + i);
        }
        check("can fill up to capacity", full.size() == BoundedStack.capacity);
        check("add when full -> returns false", !full.push("one more"));
        check("full license stays at capacity",
                full.size() == BoundedStack.capacity);
    }
    
    // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");
        
        BoundedStack examine = new BoundedStack(Arrays.asList("ABCD1234", "BADC2341"));
        check("size reports 2", examine.size() == 2);
        check("contains finds an existing license", examine.contains("ABCD1234"));
        check("contains rejects a missing license", !examine.contains("Z"));
        check("license returns the full list in order",
                examine.license().equals(Arrays.asList("ABCD1234", "BADC2341")));

        int before = examine.size();
        examine.size();
        examine.contains("ABCD1234");
        examine.license();
        check("observers have no side effects", examine.size() == before);

    }

    // --- Producer ต้องคืนตัวใหม่ ไม่แก้ตัวเดิม ---
    private static void testProducer() {
        System.out.println("\n-- Producer (shuffled) --");

        BoundedStack original = new BoundedStack(Arrays.asList("A", "B", "C", "D"));
        BoundedStack shuffled = original.shuffled();

        check("shuffled has the same size", shuffled.size() == original.size());

        List<String> boxA = new ArrayList<String>(original.license());
        List<String> boxB = new ArrayList<String>(shuffled.license());
        Collections.sort(boxA);
        Collections.sort(boxB);
        check("shuffled contains exactly the same licenses", boxA.equals(boxB));

        check("shuffled does not mutate the original",
                original.license().equals(Arrays.asList("A", "B", "C", "D")));

        // mutate ตัวใหม่ต้องไม่กระทบตัวเดิม
        shuffled.push("E");
        check("mutating the result does not affect the original",
                original.size() == 4);

        // boundary: shuffle เพลย์ลิสต์ว่างต้องไม่พัง
        BoundedStack emptyShuffled = new BoundedStack().shuffled();
        check("shuffling an empty bounded stack is safe", emptyShuffled.size() == 0);
   
    }

    // --- ทดสอบว่าไม่เกิด representation exposure ---
    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        // ขาออก: แก้ list ที่ได้จาก licenses() ต้องไม่กระทบ rep
        BoundedStack licenses = new BoundedStack();
        licenses.push("A");

        List<String> got = licenses.license();
        got.clear();
        check("clearing result of license() does not affect bounded stack",
                licenses.size() == 1);

        got = licenses.license();
        got.add("injected");
        check("adding to result of license() does not affect bounded stack",
                licenses.size() == 1 && !licenses.contains("injected"));

        // สองครั้งต้องเป็นคนละ object
        check("license() returns a fresh list each call",
                licenses.license() != licenses.license());

        // ขาเข้า: แก้ list ที่ส่งให้ constructor ต้องไม่กระทบ rep
        List<String> input = new ArrayList<String>(Arrays.asList("ABCD1234", "BADC2341"));
        BoundedStack pass = new BoundedStack(input);

        input.clear();
        check("clearing constructor argument does not affect bounded stack",
                pass.size() == 2);

        input.add("injected");
        check("adding to constructor argument does not affect bounded stack",
                !pass.contains("injected"));
    }



}
