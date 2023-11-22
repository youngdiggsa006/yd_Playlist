/**
 * @author Amaris Young-Diggs
 * @description this is the MySong class which has 3 constructors
 * that all call the super class
 *
 */

package cmsc256;

import bridges.data_src_dependent.Song;

public class MySong extends Song implements Comparable<MySong>{

    /**
     * default constructor
     * calls super
     */
    public MySong(){
        super();
    }

    /**
     * calls super
     * @param artist artist of the song
     * @param song the song
     * @param album the album
     * @param lyrics the lyrics for a song
     * @param release_date the release date
     */
    public MySong(String artist, String song, String album, String lyrics, String release_date){
        super(artist, song, album, lyrics, release_date);
    }

    /**
     * takes in a song objects and gets the super class
     * @param song
     */
    public MySong(Song song){
        super(song.getArtist(),song.getSongTitle(), song.getAlbumTitle(), song.getLyrics(),song.getReleaseDate() );
    }

    /**
     * used to sort alphabetically
     * @param o the object to be compared.
     * @return an integer, 1, 0, or -1
     */

    @Override
    public int compareTo(MySong o) {
        return this.getSongTitle().compareTo(o.getSongTitle());
    }
}
