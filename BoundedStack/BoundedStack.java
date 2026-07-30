import java.util.*;
// ตวรจสอบทะเบียนรถ
/**
 * BoundedStack คือสแตกที่เก็บ String ได้ไม่เกิน capacity ตัว
 * ที่กำหนดตอนสร้าง 
 */
public class BoundedStack {
//       ===== representation =====
    private final List<String> license ;
    public static final int capacity = 100 ;
    public static final int MAX_LEN = 8;    
    
    /** 
    //AF license คือ เลขป้ายทะเบียนรถ
    //     capacity คือ จำนวนป้ายทะเบียนสูงสุดที่เก็บได้
    //      MAX_LEN คือ จำนวนตัวเลขและอักษรสูงสุด
    //RI 1. license != null                                  (ต้องมีอยู่จริง)
    //   2. ไม่มีสมาชิกใน license เป็น null                    (ไม่มีlicenseใดเป็น null)
    //   3. ไม่มีสมาชิกใน license เป็นสตริงว่าง "" และ ไม่มีสมาชิกใน license ซ้ำกัน       (ไม่มีlicenseที่เป็นสตริงว่างหรือlicenseห้ามซ้ำกัน)
    //   4. ตัวอักษร license ต้องไม่เกิน MAX_LEN                   (ความจุต้องไม่เกิน 8 ตัวอักษร)
    //   5. license.size() <= capacity                        (ห้ามเกินความจุ)
    //SF
    //   - license ถูกประกาศเป็น private final จึงไม่มีการเปลี่ยนของ field นี้ได้จากภายนอก
    //   - constructor ที่รับ List<String> จากภายนอก จะคัดลอกข้อมูลใส่ ArrayList ใหม่
    //     แทนที่จะเก็บ ของ list ที่ส่งมาโดยตรง ดังนั้นการแก้ list ต้นฉบับ
    //     ของ client ในภายหลังจะไม่กระทบ rep 
    //   - เมธอด license() (ขาออก) คืนค่าเป็นสำเนา (copy) ของ license ไม่ใช่ reference ตรง ๆ
    //     ดังนั้นผู้เรียกจะแก้ไข list ที่คืนกลับไปไม่ส่งผลต่อ rep ภายใน
    //   - String เป็น immutable อยู่แล้ว จึงไม่ต้องกังวลเรื่องการแก้ไของค์ประกอบภายใน list
    */
    
    private void checkRep() {
        assert license != null : "license is not null";
        assert license.size() <= capacity : "license count must not exceed capacity";
      
        Set<String> seen = new HashSet<>();
        for (String set : license) {
            assert set != null : "license ต้องไม่มีสมาชิกเป็น null";
            assert !set.isEmpty() : "license ต้องไม่มีสมาชิกเป็นสตริงว่าง";
            assert set.length() <= MAX_LEN : "license ต้องไม่เกิน 8 ตัวอักษร";
            assert seen.add(set) : "Dupplicate license: " + set;
        }

     }
    // ===== Creator =====
    
    public BoundedStack() {

        this.license = new ArrayList<>();
        checkRep();
    } 
    /**
     * @param initial รายชื่อlicense เริ่มต้น ต้องไม่ซ้ำและไม่เกิน capacity
     * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
     */
    public BoundedStack(List<String> initial) {
       if (initial == null || initial.size() > capacity) throw new IllegalArgumentException("initial must not be null or initial must not exceed capacity");
        Set<String> seen = new HashSet<>();
       for (String set : initial) {
            if (set == null || set.isEmpty() ) throw new IllegalArgumentException("initial must not contain null or empty license");
            if (set.length() > MAX_LEN) throw new IllegalArgumentException("initial must not contain license exceeding 8 characters");
            if (!seen.add(set)) throw new IllegalArgumentException("initial must not contain duplicate license");
        }
        this.license = new ArrayList<>(initial); 
        checkRep();
 }
    // ===== Mutators =====
 /**  
 * เพิ่ม licenses เข้าไปบนสุดของสแตก
 * @param licenses สมาชิกที่จะ push, ต้องไม่เป็น null
 * @return true ถ้า push สำเร็จ, false ถ้า licenses ซ้ำกับสมาชิกที่มีอยู่แล้วหรือสแตกเต็มแล้ว
 * @throws IllegalArgumentException ถ้า licenses เป็น null
 */
public boolean push(String licenses) {
    if (licenses == null || licenses.isEmpty()) throw new IllegalArgumentException("cannot push null or empty license");
    if (licenses.length() > MAX_LEN ) return false; // ไม่ throw exception แต่ return false ถ้าเกิน 8 ตัวอักษร
    if (license.size() >= capacity || license.contains(licenses)) return false;
    license.add(licenses);
    checkRep();
    return true;
}
    //===== Observers =====

    public int size() {
        return license.size();
    }

    public boolean contains(String licenses) {
        return license.contains(licenses);
    }

    public List<String> license() {
        return new ArrayList<>(license);
    }

    // ===== Producer =====
    /**
    * สร้าง BoundedStack ใหม่ที่มีสมาชิกเหมือนเดิมแต่สลับลำดับ
     * @return BoundedStack ใหม่ที่สลับลำดับแล้ว
     */
    public BoundedStack shuffled() {
        List<String> copy = new ArrayList<>(license);
        Collections.shuffle(copy);
        return new BoundedStack(copy);
    }

    @Override
    public String toString() {
        return license.toString();
    }
}
