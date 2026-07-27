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
        testRemove();
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

        BoundedStack empty = new BoundedStack();

        check("license() -> empty", empty.size() == 0);
        check("license() -> contains nothing", !empty.contains("anything"));

        BoundedStack b = new BoundedStack(Arrays.asList("Aaaaaaaa", "B", "C"));
        check("new(list) -> size 3", b.size() == 3);
        check("new(list) -> contains B", b.contains("B"));
        check("new(list) -> preserves order",
                b.license().equals(Arrays.asList("Aaaaaaaa", "B", "C")));

        boolean threw = false;
        try {
            new BoundedStack(Arrays.asList("A", "A"));
        } catch (IllegalArgumentException e) {
            threw = true;
        }
        check("new(duplicates license) -> throws IllegalArgumentException", threw);
        /* 
        check("new() -> empty", empty.size() == 0);
        check("new() -> contains nothing", !empty.contains("anything"));

        BoundedStack b = new BoundedStack(Arrays.asList("A", "B", "C"));
        check("new(list) -> size 3", b.size() == 3);
        check("new(list) -> contains B", b.contains("B"));
        check("new(list) -> preserves order",
                b.license().equals(Arrays.asList("A", "B", "C")));

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
        */
    }

    // --- Mutator:  ---
    private static void testAdd() {
        System.out.println("\n-- Add --");

        BoundedStack s = new BoundedStack();
/*
        check("add(A) -> returns true", s.push("A"));
        check("add(A) -> size 1", s.size() == 1);
        check("add(A) -> found by contains", s.contains("A"));

        s.push("B");
        s.push("C");
        check("add preserves insertion order",
                s.license().equals(Arrays.asList("A", "B", "C")));

        // เพลงซ้ำไม่ใช่ error — คืน false เฉย ๆ
        check("add duplicate -> returns false", !s.push("A"));
        check("failed add leaves size unchanged", s.size() == 3);

        // input ที่ผิดเงื่อนไขต้องโยน exception
        boolean threwEmpty = false;
        try {
            s.push("");
        } catch (IllegalArgumentException e) {
            threwEmpty = true;
        }
        check("add(empty string) -> throws IllegalArgumentException", threwEmpty);

        boolean threwNull = false;
        try {
            s.push(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        check("failed adds leave license unchanged", s.size() == 3);

        // boundary: เติมจนเต็มพอดีแล้วเติมเพิ่ม
        BoundedStack full = new BoundedStack();
        for (int i = 0; i < BoundedStack.capacity; i++) {
            full.push("license" + i);
        }
        check("can fill up to capacity", full.size() == BoundedStack.capacity);
        check("add when full -> returns false", !full.push("one more"));
        check("full license stays at capacity",
                full.size() == BoundedStack.capacity);
    */
    }

    private static void testRemove() {
        System.out.println("\n-- Remove --");
/*
        BoundedStack s = new BoundedStack(Arrays.asList("A", "B", "C"));
        check("remove(B) -> returns true", s.remove("B"));
        check("remove -> size decreases", s.size() == 2);
        check("remove -> license is gone", !s.contains("B"));
        check("remove keeps the others in order",
                s.license().equals(Arrays.asList("A", "C")));

        // ลบlicenseที่ไม่มีไม่ใช่ error — คืน false เฉย ๆ
        check("remove missing license -> returns false", !s.remove("nope"));
        check("failed remove leaves size unchanged", s.size() == 2);

        // boundary: ลบจนหมด
        s.remove("A");
        s.remove("C");
        check("remove all -> empty", s.size() == 0);
        check("remove on empty license -> returns false", !s.remove("A"));
*/
    }

    // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");
/* 
        BoundedStack s = new BoundedStack(Arrays.asList("A", "B"));
        check("size reports 2", s.size() == 2);
        check("contains finds an existing license", s.contains("A"));
        check("contains rejects a missing license", !s.contains("Z"));
        check("license returns the full list in order",
                s.license().equals(Arrays.asList("A", "B")));

        int before = s.size();
        s.size();
        s.contains("A");
        s.license();
        check("observers have no side effects", s.size() == before);
*/
    }

    // --- Producer ต้องคืนตัวใหม่ ไม่แก้ตัวเดิม ---
    private static void testProducer() {
        System.out.println("\n-- Producer (shuffled) --");
/*
        BoundedStack original = new BoundedStack(Arrays.asList("A", "B", "C", "D"));
        BoundedStack shuffled = original.shuffled();

        check("shuffled has the same size", shuffled.size() == original.size());

        List<String> a = new ArrayList<String>(original.license());
        List<String> b = new ArrayList<String>(shuffled.license());
        Collections.sort(a);
        Collections.sort(b);
        check("shuffled contains exactly the same licenses", a.equals(b));

        check("shuffled does not mutate the original",
                original.license().equals(Arrays.asList("A", "B", "C", "D")));

        // mutate ตัวใหม่ต้องไม่กระทบตัวเดิม
        shuffled.push("E");
        check("mutating the result does not affect the original",
                original.size() == 4);

        // boundary: shuffle เพลย์ลิสต์ว่างต้องไม่พัง
        BoundedStack emptyShuffled = new BoundedStack().shuffled();
        check("shuffling an empty bounded stack is safe", emptyShuffled.size() == 0);
   */
    }

    // --- ทดสอบว่าไม่เกิด representation exposure ---
    private static void testExposure() {
        System.out.println("\n-- Representation Exposure --");

        /* 
        // ขาออก: แก้ list ที่ได้จาก songs() ต้องไม่กระทบ rep
        BoundedStack s = new BoundedStack();
        s.push("A");

        List<String> got = s.license();
        got.clear();
        check("clearing result of license() does not affect bounded stack",
                s.size() == 1);

        got = s.license();
        got.add("injected");
        check("adding to result of license() does not affect bounded stack",
                s.size() == 1 && !s.contains("injected"));

        // สองครั้งต้องเป็นคนละ object
        check("license() returns a fresh list each call",
                s.license() != s.license());

        // ขาเข้า: แก้ list ที่ส่งให้ constructor ต้องไม่กระทบ rep
        List<String> input = new ArrayList<String>(Arrays.asList("A", "B"));
        BoundedStack p = new BoundedStack(input);

        input.clear();
        check("clearing constructor argument does not affect bounded stack",
                p.size() == 2);

        input.add("injected");
        check("adding to constructor argument does not affect bounded stack",
                !p.contains("injected"));
                */
    }



}
