import java.util.*;
// ตวรจสอบทะเบียนรถ
/**
 * BoundedStack คือสแตกที่เก็บ String ได้ไม่เกิน capacity ตัว
 * ที่กำหนดตอนสร้าง 
 */
public class BoundedStack {
//       ===== representation =====
    private final List<String> license ;
    private final int capacity  ;
    /** 
    //AF license คือ เลขป้ายทะเบียนรถ
    //     capacity คือ จำนวนตัวอักษร
    
    //RI 1. license != null                                  (ต้องมีอยู่จริง)
    //   2. ไม่มีสมาชิกใน license เป็น null                    (ไม่มีlicenseใดเป็น null)
    //   3. ไม่มีสมาชิกใน license เป็นสตริงว่าง "" และ ไม่มีสมาชิกใน license ซ้ำกัน       (ไม่มีlicenseที่เป็นสตริงว่างหรือlicenseห้ามซ้ำกัน)
    //   4. capacity >= 0                                     (ความจุต้องไม่ติดลบ)
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
        assert license.size() <= capacity;
        assert capacity >= 0 : "capacity must not be negative";
        Set<String> seen = new HashSet<>();
        for (String s : license) {
            assert s != null : "license ต้องไม่มีสมาชิกเป็น null";
            assert s != "" : "license ต้องไม่มีสมาชิกเป็นสตริงว่าง";
            assert seen.add(s) : "Dupplicate license: " + s;
        }

     }
    // ===== Creator =====
    
    public BoundedStack () {
        this.license = new ArrayList<>();
        checkRep();
    } 
   /**
     * @param initial รายชื่อเพลงเริ่มต้น ต้องไม่ซ้ำและไม่เกิน capacity 
     * @throws IllegalArgumentException ถ้า initial ผิดเงื่อนไข
     */
    public  BoundedStack(List<String> initial,int capacity) {
       if (initial == null) {
            throw new IllegalArgumentException("initial must not be null");
        }
        for (String s : initial) {
            if (s == null || s.isEmpty()) {
                throw new IllegalArgumentException("initial must not contain null or empty license");
            }
        }
        if (new HashSet<>(initial).size() != initial.size()) {
            throw new IllegalArgumentException("initial must not contain duplicate songs");
        }
        if (initial.size() > capacity) {
            throw new IllegalArgumentException("initial must not exceed capacity");
        }
        this.capacity = capacity;
        this.capacity = new ArrayList<>(initial); 
        checkRep();
 }
    // ===== Mutators =====
 /**  
 * เพิ่ม s เข้าไปบนสุดของสแตก
 * @param s สมาชิกที่จะ push, ต้องไม่เป็น null
 * @return 
 * @throws IllegalArgumentException ถ้า s เป็น null
 * @throws IllegalStateException ถ้าสแตกเต็มแล้ว (size() == capacity)
 */
public boolean push(String s) {
    if (s == null) {
        throw new IllegalArgumentException("cannot push null");
    }
    if (license.size() >= capacity) {
        throw new IllegalStateException("stack is full, capacity = " + capacity);
    }
    license.add(s);
    checkRep();
    return true;
}
/**
     * @param licenses เลขป้ายทะเบียนรถ
     * @return true ลบสำเร็จ, false ถ้าไม่พบ
     */
    public boolean remove(String licenses) {
        boolean removed = license.remove(licenses);
        checkRep();
        return removed;
    }

    //===== Observers =====














    // ===== Producer =====


 
















}
