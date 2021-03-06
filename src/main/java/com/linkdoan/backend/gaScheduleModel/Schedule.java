package com.linkdoan.backend.gaScheduleModel;

import com.linkdoan.backend.service.impl.ScheduleServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

public class Schedule {

    private double fitness = 0;
    private final int MAX_SCORE = 5;
    private final int numberOfCrossoverPoints = 5;
    private final int mutationSize = 5;

    private static final int DAY_NUM = 5;
    private static final int DAY_HOURS = ScheduleServiceImpl.getDAY_HOURS();
    private static final int ROOM_NUM = ScheduleServiceImpl.getROOM_NUM();

    private Vector<ArrayList<CourseClass>> slots;

    private Vector<Integer> classes;

    private Vector<Boolean> criteria = new Vector<>();

    private final InputFromFile inputFromFile;

    public Schedule(InputFromFile inputFromFile) {
        slots = new Vector();
        classes = new Vector();
        criteria = new Vector();
        this.inputFromFile = inputFromFile;
    }

    public InputFromFile getInputFromFile() {
        return this.inputFromFile;
    }

    public int getNumberOfCrossoverPoints() {
        return numberOfCrossoverPoints;
    }

    public int getMutationSize() {
        return mutationSize;
    }

    public double getFitness() {
        return fitness;
    }

    public Vector<ArrayList<CourseClass>> getSlots() {
        return slots;
    }

    public Vector<Integer> getClasses() {
        return classes;
    }

    public Vector<Boolean> getCriteria() {
        return this.criteria;
    }

    public void setClasses(Vector<Integer> classes) {
        this.classes = classes;
    }

    public void setSlots(Vector<ArrayList<CourseClass>> slots) {
        this.slots = slots;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public void setCriteria(Vector<Boolean> criteria) {
        this.criteria = criteria;
    }


    //Chuyen tu (HashMap) classes sang (Vector) slots
    private void fromClassesToSlots() {
        for (int i = 0; i < DAY_HOURS * DAY_NUM * ROOM_NUM; i++) {
            slots.add(new ArrayList());
        }
        //Duyet cac phan tu cua HashMap
        ArrayList<CourseClass> listClass = inputFromFile.getClassList();
        for (int i = 0; i < listClass.size(); i++) {
            CourseClass cc = listClass.get(i);
            int duration = cc.getDuration();
            int possion = classes.get(i);
            //Duyet cac tiet cua lop tu tiet bat dau den het thoi gian hoc cua lop
            for (int j = 0; j < duration; j++) {
                slots.get(possion + j).add(cc);
            }
        }
    }

    //---Basic init
//    public void initializeSchedule() {
//        ArrayList<CourseClass> listClass = InputFromMySQL.getClassList();
//        Random rd = new Random();
//        for( int i=0; i < listClass.size(); i++) {
//            int dur = listClass.get(i).getDuration();
//            int day = rd.nextInt(DAY_NUM);
//            int room = rd.nextInt(ROOM_NUM);
//            int time = rd.nextInt(DAY_HOURS/2 + 1 - dur);
//            switch (rd.nextInt(2)) {
//                case 0 :
//                    break;
//                case 1:
//                    time += DAY_HOURS/2;
//                    break;
//            }
//            int pos = day * ROOM_NUM * DAY_HOURS + room * DAY_HOURS + time;
//            classes.add(pos);
//        }
//        fromClassesToSlots();
//        Fitness();
//    }
    //---Advanced init
    //Tao ra NST ngau nhien : Cac lop xep sat nhau sao cho 1 lop chi hoc 1 phong trong pham vi 5 tiet mot
    public void initializeSchedule() {
        //Tao danh sach cac lop voi so tiet bang nhau, cac day 3 , 2 tiet
        ArrayList<CourseClass> listClass = inputFromFile.getClassList();
        System.out.println("COURSE LIST:--------------");
        for (int i = 0; i < listClass.size(); i++) {
            System.out.println("Class: " + listClass.get(i).getCourse().getName() + " duration: " + listClass.get(i).getDuration());
        }
//        ArrayList<CourseClass> cc4 = new ArrayList();
        ArrayList<CourseClass> cc3 = new ArrayList();
        ArrayList<CourseClass> cc2 = new ArrayList();
        for (CourseClass i : listClass) {
            switch (i.getDuration()) {
                case 3:
                    cc3.add(i);
                    break;
                case 2:
                    cc2.add(i);
                    break;
            }
        }

        for (int i = 0; i < cc3.size(); i++) {
            System.out.println("Classs: " + cc3.get(i).getCourse().getName() + " duration: " + cc3.get(i).getDuration());
        }

        //Xao tron cac danh sach lop hoc
        Collections.shuffle(cc3);
        Collections.shuffle(cc2);

//        int size4 = cc4.size();
        int size3 = cc3.size();
        int size2 = cc2.size();
        int i = 0, j = 0, k = 0; // i,j,k la vi tri dang xet trong danh sach cac lop tren
        int next = 0;            // next la vi tri dang xet trong slots
        listClass.stream().forEach((_item) -> {
            this.getClasses().add(null);
        });
        Random rd = new Random();
        //Lay ngau nhien lop tu cac danh sach tren
        for (int x = 0; x < listClass.size(); ) {
            switch (rd.nextInt(3) + 2) {
                case 2:
                    System.out.println("CASE 2");
                    //Neu random so 2 thi kiem tra xem con lop  3 tiet khong, neu khong con thi break de random lop khac
                    if (i < size2) {
                        classes.set(listClass.indexOf(cc2.get(i++)), next);
                        next += 2;
                        x++;
                        //Kiem tra xem con lop 3 tiet khong, neu con thi set vao ngay sau lop 2 tiet tren
                        if (j < size3) {
                            classes.set(listClass.indexOf(cc3.get(j++)), next);
                            next += 3;
                            x++;
                        }
                        //Neu cung khong thi kiem tra neu con 1 lop 2 tiet thi lai set vao ngay sau lop 2 tiet bd
                        else if (i < size2) {
                            classes.set(listClass.indexOf(cc2.get(i++)), next);
                            next += 3;
                            x++;
                            //Neu khong con TH nao thi +3 break
                        } else {
                        }
                    } else {
                    }
                case 3:
                    System.out.println("CASE 3");
                    //Neu random so 3 thi kiem tra xem con lop  3 tiet khong, neu khong con thi break de random lop khac
                    if (j < size3) {
                        classes.set(listClass.indexOf(cc3.get(j++)), next);
                        next += 3;
                        x++;
                        //Kiem tra xem con lop 2 tiet khong, neu con thi set vao ngay sau lop 3 tiet tren
                        if (i < size2) {
                            classes.set(listClass.indexOf(cc2.get(i++)), next);
                            next += 2;
                            x++;
                        } else {
                            System.out.println("NOT FOUND CLASS WITH 2");
                            next += 2;
                        }
                    } else {
                    }
            }
        }
        //Tao vector tu HashMap
        fromClassesToSlots();
        System.out.println("from classes to slots ok");
        Fitness();
        System.out.println("fitness ok to slots ok");
        System.out.println("initializeDSchedule DONE!!!");
    }

    public void Fitness() {
        int score = 0;
        int daySize = DAY_HOURS * ROOM_NUM;
        int ci = 0;

        ArrayList<CourseClass> listClass = this.inputFromFile.getClassList();
        for (int x = 0; x < listClass.size(); x++) {
            int p = classes.get(x);
            int day = p / daySize;
            int time = p % daySize;
            int room = time / DAY_HOURS;
            time = time % DAY_HOURS;

            CourseClass cc = listClass.get(x);
            int dur = cc.getDuration();

            //Kiem tra phong trong (+1)
            boolean ro = false;
            for (int i = 0; i < dur; i++) {
                if (slots.get(p + i).size() > 1) {
                    ro = true;
                    break;
                }
            }
            if (!ro) {
                score++;
            }
            criteria.add(ci + 0, !ro);

            //Kiem tra phong co du cho khong? (+1)
            System.out.println("room: " + room);
            Room r = getInputFromFile().getRoomById(room);
            if (getInputFromFile() == null) System.out.println("input is null");
            if (r == null) System.out.println("room null");
            getCriteria().add(ci + 1, r.getNumberOfSeats() >= cc.getNumberOfSeats());
            if (getCriteria().get(ci + 1)) {
                score++;
            }

            //Kiem tra phong co lab khong (+1)
            getCriteria().add(ci + 2, !cc.isLabRequired() || (cc.isLabRequired() && r.isLab()));
            if (getCriteria().get(ci + 2)) {
                score++;
            }
            //Kiem tra lop co bi trung giao vien tiet nao khong (+1)
            //va bi trung nhom SV tiet nao khong (+1)
            boolean po = false, go = false;
            total_overlap:
            for (int i = 0, t = day * daySize + time; i < ROOM_NUM; i++, t += DAY_HOURS) {
                for (int j = 0; j < dur; j++) {
                    //Kiem tra trung lap voi cac lop khac tung tiet
                    ArrayList<CourseClass> lc = slots.get(t + j);
                    if (lc.isEmpty()) {
                        continue;
                    }
                    for (int k = 0; k < lc.size(); k++) {
                        if (cc != lc.get(k)) {

                            if (!po && cc.professorOverlaps(lc.get(k))) {
                                po = true;
                            }
                            if (!go && cc.groupsOverlaps(lc.get(k))) {
                                go = true;
                            }
                            if (po && go) {
                                break total_overlap;
                            }
                        }
                    }
                }
            }

            if (!po) {
                score++;
            }
            criteria.add(ci + 3, !po);
            if (!go) {
                score++;
            }
            criteria.add(ci + 4, !go);
            ci += 5;
        }
        fitness = (float) score / ((float) listClass.size() * MAX_SCORE);
    }

    //Tinh so lan vi pham rang buoc mem ( O day chi xet rang buoc mem la giao vien day o 2 noi xa nhau trong cung 1 ngay )
    public int softConflict() {
        int soft_conflict = 0;
        int daySize = DAY_HOURS * ROOM_NUM;
        ArrayList<CourseClass> classList = inputFromFile.getClassList();
        //Xet tung giao vien xem co vi pham rang buoc mem khong?
        for (Professor pr : inputFromFile.getProfList()) {
            ArrayList<CourseClass> class_list = pr.getCourseClasses();
            for (int i = 0; i < class_list.size() - 1; i++) {

                int index1 = classList.indexOf(class_list.get(i));
                int day1 = this.getClasses().get(index1) / daySize;
                int room1 = (this.getClasses().get(index1) % daySize) / DAY_HOURS;

                for (int j = i + 1; j < class_list.size(); j++) {

                    int index2 = classList.indexOf(class_list.get(j));
                    int day2 = this.getClasses().get(index2) / daySize;
                    int room2 = (this.getClasses().get(index2) % daySize) / DAY_HOURS;

                    if ((day2 == day1) && (inputFromFile.getRoomById(room1).getDistance() != inputFromFile.getRoomById(room2).getDistance())) {
                        soft_conflict++;
                    }
                }
            }
        }
        return soft_conflict;
    }

    public void showSchedule() {
        for (int i = 0; i < DAY_HOURS * DAY_NUM * ROOM_NUM; i++) {
            System.out.println("************" + i);
            for (CourseClass cc : slots.get(i)) {
//                    System.out.println(cc.getProfessor().getName());
//                    System.out.println(cc.getCourse().getName());
//                    cc.getGroups().stream().forEach((sg) -> {
//                        System.out.println(sg.getName());
//                    });
//                    if (cc.isLabRequired()) {
//                        System.out.println("lab");
//                    }
                System.out.println("Room Id:" + (i % (DAY_HOURS * DAY_NUM * ROOM_NUM)));
                System.out.println("Duration :" + cc.getDuration());
                System.out.println(cc.getNumberOfSeats());
                System.out.println("##");
            }

        }
    }

    //---Basic Crossover
//    public Schedule Crossover(Schedule parent2) {
//        if (this == parent2) {
//            return this;
//        } else {
//            Random rd = new Random();
//            //tao con moi
//            Schedule child = new Schedule();
//            int size = classes.size();
//            Vector<Boolean> cp = new Vector();
//            for (int i = 0; i < size; i++) {
//                cp.add(false);
//            }
//
//            //tao cac diem lai ghep danh dau TRUE
//            for (int i = numberOfCrossoverPoints; i > 0; i--) {
//                while (true) {
//                    int p = rd.nextInt(size);
//                    if (!cp.get(p)) {
//
//                        cp.set(p, Boolean.TRUE);
//                        break;
//                    }
//                }
//            }
//            boolean first = rd.nextBoolean();
//            for (int i = 0; i < size; i++) {
//                if (first) {
//                    child.classes.add(classes.get(i));
//                } else {
//
//                    child.classes.add(parent2.classes.get(i));
//                }
//                if (cp.get(i)) {
//                    first = !first;
//                }
//            }
//            child.fromClassesToSlots();
//            child.Fitness();
//
//            return child;
//        }
//    }
    //---Advanced Crossover
    public Schedule Crossover(Schedule parent2) {
        if (this == parent2) {
            return this;
        } else {
            //tao con moi
            Schedule child = new Schedule(this.inputFromFile);
            int size = classes.size();
            Vector<Boolean> cp = new Vector();
            for (int i = 0; i < size; i++) {
                cp.add(false);
            }
            //Cac diem co score_class == 5 dc giu lai, cac diem con lai lai ghep
            for (int i = 0; i < size; i++) {
                int score_class = 0;
                for (int j = 0; j < 5; j++) {
                    if (criteria.get(5 * i + j)) {
                        score_class++;
                    }
                }
                if (score_class == 5) {
                    cp.set(i, Boolean.TRUE);
                }
            }

            for (int i = 0; i < size; i++) {
                if (cp.get(i)) {
                    child.classes.add(classes.get(i));
                } else {

                    child.classes.add(parent2.classes.get(i));
                }
            }
            child.fromClassesToSlots();
            child.Fitness();

            return child;
        }
    }

    //---Mutation
//    public void Mutation() {
//        Random rd = new Random();
//        ArrayList<CourseClass> listClass = InputFromMySQL.getClassList();
//
//        int numberOfClasses = listClass.size();
//
//        for (int i = mutationSize; i > 0; i--) {
//            int mpos = rd.nextInt(numberOfClasses);
//            //current
//            CourseClass cc1 = listClass.get(mpos);
//
//            // determine position of class randomly
//            int dur = cc1.getDuration();
//            int day = rd.nextInt(DAY_NUM);
//            int room = rd.nextInt(ROOM_NUM);
//            int time = rd.nextInt(DAY_HOURS/2 + 1 - dur);
//            switch (rd.nextInt(2)) {
//                case 0 :
//                    break;
//                case 1:
//                    time += DAY_HOURS/2;
//                    break;
//            }
//            int pos = day * ROOM_NUM * DAY_HOURS + room * DAY_HOURS + time;
//            classes.set(mpos, pos);
//            this.fromClassesToSlots();
//        }
//        Fitness();
//    }

    public void Mutation() {
        Random rd = new Random();
        ArrayList<CourseClass> listClass = inputFromFile.getClassList();

        int numberOfClasses = listClass.size();

        for (int i = mutationSize; i > 0; i--) {
            int mpos = rd.nextInt(numberOfClasses);
            //current
            CourseClass cc1 = listClass.get(mpos);

            // determine position of class randomly
            int dur = cc1.getDuration();
            int day = rd.nextInt(DAY_NUM);
            int room = rd.nextInt(ROOM_NUM);
            int time = rd.nextInt(DAY_HOURS / 2 + 1 - dur);
            switch (rd.nextInt(2)) {
                case 0:
                    break;
                case 1:
                    time += DAY_HOURS / 2;
                    break;
            }
            int pos = day * ROOM_NUM * DAY_HOURS + room * DAY_HOURS + time;
            classes.set(mpos, pos);
        }
        this.fromClassesToSlots();
        Fitness();
    }
}

