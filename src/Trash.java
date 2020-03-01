/*
int[] zeros = new int[noDays];
        for(int i=0; i<noDays; i++){
        zeros[i] = 0;
        }

        int dayS = 0;
        List<Library> copyl = new ArrayList<>(libraries);
        while(dayS <= noDays && copyl.size()>0){
        Library L = null;
        int mSum = Integer.MAX_VALUE;
        int min = copyl.stream()
        .min(Comparator.comparingInt(l -> l.processTime))
        .get().processTime;
        List<Library> filtered = copyl.stream()
        .filter(l -> l.processTime == min)
        .collect(Collectors.toList());
        for(Library l : filtered){
        int dayF = dayS + l.processTime;
        int score = penalties[l.id][noDays-1];
        if(dayS+l.processTime < noDays)
        score = penalties[l.id][dayS+l.processTime];
        if(score < mSum){
        L = l;
        mSum = score;
        }
        }
        if(L != null) {
        usedLibraries.add(L);
        copyl.remove(L);
        dayS = dayS + L.processTime;
        penalties[L.id] = zeros;
        }
        }*/
