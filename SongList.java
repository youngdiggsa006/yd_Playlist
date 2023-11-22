/**
 * @author Amaris Young-Diggs
 * @description this project creates a songlist (playlist) where songs
 * can be added to the end, removed, inserted, and cleared. Also can obtain
 * songs by a certain artist.
 *
 */


package cmsc256;


import bridges.base.SLelement;
import bridges.connect.Bridges;
import bridges.connect.DataSource;
import bridges.data_src_dependent.Song;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;


public class SongList implements List<MySong>, Iterable<MySong> {
    /**
     * instance vars
     * @name playlist name
     * @firstSong is the head of the list
     * @lastSong is the tail of the list
     */
    private String name;
    private SLelement<MySong> firstSong;
    private SLelement<MySong> lastSong;
    private int numSongs;


    /**
     * default constructor for SongList; clears all instance vars
     */
    public SongList(){
       clear();
    }

    /**
     * parameterized constructor for SongList
     * @param name
     * @param song
     */
    public SongList(String name, MySong song){
        this.name = name;
        this.firstSong = new SLelement<MySong>(song);
        this.lastSong = firstSong;
        numSongs = 1;
    }


    /**
     * getter method for the name
     * @return playlist name
     */
    public String getPlaylistName(){
        return this.name;
    }

    /**
     * inner class
     */
    private class SongIterator implements Iterator<MySong> {
        /**
         * @position is the position of the element
         * @tempIterator the SLelement<MySong> element
         */
        int position;
        SLelement<MySong> tempIterator;

        /**
         * default constructor for the SongIterator
         */
        public SongIterator(){
            position = 0;
            tempIterator = firstSong;
        }

        /**
         * if the next element isn't then return true
         * @return true or false
         */
        @Override
        public boolean hasNext() {
            if(tempIterator.getNext() != null){
                return true;
            }
            return false;
        }

        /**
         * checks to see if there is a next element
         * increments the position and returns the value
         * @return the song obj
         */
        @Override
        public MySong next() {
            if(hasNext()){
                position++;
                return tempIterator.getNext().getValue();
            }else{
                throw new NoSuchElementException();
            }
        }
    }


    /**
     * method that returns a formatted list of all the songs by an artist
     * that appear on the linked list (name given as a String parameter to the method),
     * in alphabetical order according to song title
     * @param artist
     * @return string of artists
     * linked list is all the songs
     * iterate through linked list to find songs by artist
     */
    public String getSongsByArtist(String artist){

        String result = "";
        SLelement<MySong> currentSongList = firstSong;
        SLelement<MySong> tempSongList;
        SLelement<MySong> nextSongList = lastSong;
        while(currentSongList != null){
            if(currentSongList.getValue().getArtist().equals(artist)){
                if(currentSongList.getValue().getSongTitle().compareTo(currentSongList.getNext().getValue().getSongTitle()) > 0){
                    tempSongList = currentSongList;
                    currentSongList = currentSongList.getNext();
                    nextSongList = tempSongList;
                }
                    result += "Title: " + currentSongList.getValue().getSongTitle() + " Album: " + currentSongList.getValue().getAlbumTitle() + "\n";
            }
            currentSongList = currentSongList.getNext();
        }
        if(result.equals("")){
            result = "There are no songs by " + artist + " in this playlist.";
        }
        return result;
    }

    /**
     * main method
     * @param args
     */
    public static void main(String[] args){
        Bridges bridges = new Bridges(3, "ayoungdiggs12", "1614316138913");
        DataSource ds = bridges.getDataSource();
        ArrayList<Song> arrSong = null;
        MySong singleSong = new MySong();
        SongList myPlaylist = new SongList("Playlist1", singleSong);

        try{
            arrSong = ds.getSongData();
        }catch(IOException e){
            throw new RuntimeException(e);
        }

        for(Song song: arrSong) {
            MySong songObj = new MySong(song);
            myPlaylist.add(songObj);
        }
        System.out.println("Songs by Drake");
        System.out.println(myPlaylist.getSongsByArtist("Drake"));

    }

    @Override
    public Iterator<MySong> iterator() {
        return new SongIterator();
    }

    /**
     * 	Remove all contents from the list, so it is once again empty
     */
    @Override
    public void clear() {
        name = "";
        firstSong = null;
        lastSong = null;
        numSongs = 0;

    }

    /**
     * 	Inserts element at the given position
     * @param 	it
     * @param 	position
     * @return	true if successful
     * @throws IllegalArgumentException if position is < 0 or > number of elements in the list
     */
    @Override
    public boolean insert(MySong it, int position) {
        if(position < 0 || position > size()){
            throw new IllegalArgumentException();
        }
        SLelement<MySong> song = new SLelement<MySong>();

        //if the position equals 1 then set the next element equal to the firstSong
        //set firstSong equal to song and increment the number of songs
        if(position == 1){
            firstSong.setNext(firstSong);
            firstSong = song;
            numSongs++;
            return true;
        }
        //temp song equals the element after first song
        //loop thru, getting each song and setting it equal to the next
        //set the last tempSong equal to the song and increment numSongs
        else{
            SLelement<MySong> tempSong = firstSong.getNext();
            for(int i = 0; i <= position; i++){
                tempSong = tempSong.getNext();
            }
            tempSong.setNext(song);
            numSongs++;
            return true;
        }
    }

    /**
     * Appends element to the end of the list
     * @param 	it
     * @return	true if successful
     */
    @Override
    public boolean add(MySong it) {
        SLelement<MySong> newer =new SLelement<MySong>(it);
        if(firstSong == null){
            firstSong = newer;
        }else{
            lastSong.setNext(newer);
            numSongs++;
        }
        lastSong = newer;
        return true;
    }

    /**
     * @param 	position
     * @return	Remove and return the element at position
     * @throws IllegalArgumentException if position is < 0 or > number of elements in the list
     */
    @Override
    public MySong remove(int position) {

        if(position < 0 || position > numSongs){
            throw new IllegalArgumentException();
        }

        MySong songObj;
        //if position is at zero then the songObj gets set equal to the head
        //the postElement is set to the next element
        //numSongs is decremented
        if(position == 0){
            songObj = firstSong.getValue();
            SLelement<MySong> postElement = firstSong.getNext();
            firstSong = postElement;
            numSongs--;
            return songObj;
        }
        //if position is at the end then the songObj gets set equal to the tail
        //the lastSong then equals null
        //numSongs is decremented
        else if(position == this.numSongs) {
            songObj = lastSong.getValue();
            lastSong = null;
            numSongs--;
            return songObj;
        }
        //temp song equals firstSong
        //loop thru until one less than the position, getting the next element with each iteration
        else{
            SLelement<MySong> tempSong = firstSong;

            for(int i = 0; i < position-1; i++){
                tempSong = tempSong.getNext();
            }

            //the removeObj gets set equal to the next song following the temp song
            //the songObj is set equal to the removedObj
            //the tempSong set the next element to the next song
            SLelement<MySong> removedObj = tempSong.getNext();
            songObj = removedObj.getValue();
            tempSong.setNext(tempSong.getNext());

            //if the next value equals null then the last song is equal to the tempSong
            //numSongs is decremented
            if(tempSong.getNext() == null){
                lastSong = tempSong;
            }
            numSongs--;
            return songObj;
        }

    }

    /**
     * @return	the number of elements in this list
     */
    @Override
    public int size() {
        int number = 0;
        SLelement<MySong> tempSong = firstSong;

        //while the tempSong isn't empty, increment the numSongs and set the temp song equal to the next song
        while(tempSong != null){
            number++;
            tempSong = tempSong.getNext();
        }
        return number;
    }

    /**
     * @return	true if this list has no elements
     */
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * @param 	target
     * @return 	true if the target element is in this list
     * 	false otherwise
     */
    @Override
    public boolean contains(MySong target) {
        SLelement<MySong> tempSong = firstSong;

        //while the temporary song isn't null and if the value equals the target then return true
        while(tempSong != null){
            if(tempSong.getValue().equals(target)){
                return true;
            }
            tempSong = tempSong.getNext();//the temporary song get sets equal to the next song
        }
        return false;
    }

    /**
     * @return	the element at the given position
     * @throws IllegalArgumentException if position is < 0 or > number of elements in the list
     */
    @Override
    public MySong getValue(int position) {
        if(position < 0 || position > this.numSongs){
            throw new IllegalArgumentException();
        }

        //if position is at the beginning of the list, return the first value of SLelement<MySong>
        if(position == 0){
            return firstSong.getValue();
        }
        //if the position equals the last position, return the last value of SLelement<MySong>
        else if(position == this.numSongs){
            return lastSong.getValue();
        }
        //create a temporary song and sets it to the first song to iterate until reaching the position
        else{
            SLelement<MySong> tempSong = firstSong;
            for(int i = 1; i < position; i++){
                tempSong = tempSong.getNext();
            }
            return tempSong.getValue();
        }
    }

}


