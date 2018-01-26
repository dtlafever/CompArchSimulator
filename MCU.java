import java.util.ArrayList;

// Memory Control Unit
// Reserved Locations of Memory:
// 0 - Reserved for the Trap instruction for Part III.
// 1 - Reserved for a machine fault
// 2 - Store PC for Trap
// 3 - Not Used
// 4 - Store PC for Machine Fault
// 5 - Not Used
public class MCU{
    // 16 bit words, so be careful
    ArrayList<Integer> memory;

    public int MAX_MEMORY = 2048;
    public int EXPANDED_MAX_MEMORY = 4096;

    // initialize all memory to 0 with a size of 2048
    public MCU(){
        this.memory = new ArrayList<Integer>(this.MAX_MEMORY);
        for (int i = 0; i < this.MAX_MEMORY; i++){
            this.memory.add(0);
        }
        //TODO: cache (i don't think this is required for project 1)
    }

    // Expand memory to 4096
    public void expandMemorySize(){
        if (this.memory != null && this.memory.size() > 0){
            this.memory.ensureCapacity(this.EXPANDED_MAX_MEMORY);
            for (int curSize = memory.size(); curSize < this.EXPANDED_MAX_MEMORY; curSize++){
                this.memory.add(0);
            }
        }
    }

    // get the current size of memory
    public int getMemSize(){
        if (this.memory != null){
            return this.memory.size();
        }else{
            return 0;
        }
    }

    // return the word from memory at a particular address
    public int getWord(int addr){
        return this.memory.get(addr);
    }

    // set a word in memory to a particular value
    public void storeWord(int addr, int value){
        if (this.memory != null){
            this.memory.set(addr, value);
        }
    }

    //TODO: cache?
}