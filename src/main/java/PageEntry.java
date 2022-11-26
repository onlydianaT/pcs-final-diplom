public class PageEntry implements Comparable<PageEntry>  {
    private String pdfName;
    private int page;
    private int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }
    @Override
    public int compareTo(PageEntry o) {
        int currentCount=o.getCount();
        if(currentCount>count){
            count=currentCount;
            return count;
        }
        return currentCount;
    }
    public String getPdfName() {
        return pdfName;
    }
    public int getPage() {
        return page;
    }
    public int getCount() {
        return count;
    }
}
