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
    //     capacity คือ จำนวนตัวอักษร
    
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
    
        //assert capacity > 0 : "capacity must be positive";
        Set<String> seen = new HashSet<>();
        for (String s : license) {
            assert s != null : "license ต้องไม่มีสมาชิกเป็น null";
            assert s != "" : "license ต้องไม่มีสมาชิกเป็นสตริงว่าง";
            assert s.length() <= MAX_LEN : "license ต้องไม่เกิน 8 ตัวอักษร";
            assert seen.add(s) : "Dupplicate license: " + s;
        }

     }
    // ===== Creator =====
    
    public BoundedStack() {
        this.license = new ArrayList<>();
        //this.capacity = 8;
        checkRep();
    } 
    /**
     * @param initial รายชื่อlicense เริ่มต้น ต้องไม่ซ้ำและไม่เกิน capacity
     * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
     */
    public BoundedStack(List<String> initial) {
       if (initial == null || initial.size() > capacity) throw new IllegalArgumentException("initial must not be null or initial must not exceed capacity");
        Set<String> seen = new HashSet<>();
       for (String s : initial) {
            if (s == null || s == "" ) throw new IllegalArgumentException("initial must not contain null or empty license");
            if (s.length() > MAX_LEN) throw new IllegalArgumentException("initial must not contain license exceeding 8 characters");
            if (!seen.add(s)) throw new IllegalArgumentException("initial must not contain duplicate license");
        }
        //this.capacity = capacity;
        this.license = new ArrayList<>(initial); 
        checkRep();
 }
    // ===== Mutators =====
 /**  
 * เพิ่ม s เข้าไปบนสุดของสแตก
 * @param s สมาชิกที่จะ push, ต้องไม่เป็น null
 * @return true ถ้า push สำเร็จ, false ถ้า s ซ้ำกับสมาชิกที่มีอยู่แล้วหรือสแตกเต็มแล้ว
 * @throws IllegalArgumentException ถ้า s เป็น null
 */
public boolean push(String s) {
    if (s == null || s == "") throw new IllegalArgumentException("cannot push null or empty license");
    if (s.length() > MAX_LEN ) return false; // ไม่ throw exception แต่ return false ถ้าเกิน 8 ตัวอักษร
    if (license.size() >= capacity || license.contains(s)) return false;
    license.add(s);
    checkRep();
    return true;
}
    /**
     * @param licenses เลขป้ายทะเบียนรถ
     * @return true ลบสำเร็จ, false ถ้าไม่พบ
     */
    public boolean remove(String licenses) {
        if (!license.contains(licenses)) return false;

        boolean removed = license.remove(licenses);
        checkRep();
        return removed;
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
