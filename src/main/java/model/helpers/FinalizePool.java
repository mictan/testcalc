package model.helpers;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;

public class FinalizePool {
    private static FinalizePool instance;
    public static FinalizePool getInstance(){
        if(instance == null){
            synchronized (FinalizePool.class){
                if(instance == null){
                    instance = new FinalizePool();
                }
            }
        }
        return instance;
    }

    private boolean finalized = false;

    private FinalizePool(){}

    private final ArrayList<Runnable> finalizers = new ArrayList<>();

    public void addFinalizer(Runnable finalizer){
        if(finalized){
            throw new IllegalStateException("Pool already finalized");
        }
        finalizers.add(finalizer);
    }

    public void addFinalizer(Closeable target){
        addFinalizer(new ClosableFinalizer(target));
    }

    public void applyFinalize(){
        finalized = true;
        for (Runnable r: finalizers){
            try{
                r.run();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static class ClosableFinalizer implements Runnable{
        private final Closeable target;

        private ClosableFinalizer(Closeable target) {
            this.target = target;
        }

        @Override
        public void run() {
            try {
                target.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
