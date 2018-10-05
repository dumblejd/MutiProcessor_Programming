package project2;

public class test {

}
semaphore service=1;
Semaphore countMutex[2] = {1,1};
Semaphore canUse[2]={1,1}
Int count[2]={0,0};

void arriveBridge(int direction){
    Down(service);

    Down(canUse[direction]);        // stuck if being let wait

    Down(countMutex[direction]);  //保护count
    count[direction]++;             // count is the running number
    if(count[direction]==1)
    {
        Down(canUse[1-direction]);  // let reverse direction wait
    }
    Up(countMutex[direction]);  //保护count
    
    Up(canUse[direction]);   //

    Up(service);
    
    
};

void leaveBridge(int direction){
    Down(countMutex[direction]);
    count[direction]--;
    If(count[direction]==0)
    {        // cars of my direction all left, let reverse direction go
     Up(canUse[1-direction]);
    } 
    Up(countMutex[direction]);
}