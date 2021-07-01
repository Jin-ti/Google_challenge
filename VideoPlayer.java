package com.google;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class VideoPlayer {

  private final VideoLibrary videoLibrary;
  private Video playingVideo;
  private Video pausingVideo;
  private Map<String, VideoPlaylist> playlists;

  public VideoPlayer() {
    this.videoLibrary = new VideoLibrary();
    playingVideo = null;
    pausingVideo = null;
    playlists = new HashMap<>();
  }

  public void numberOfVideos() {
    System.out.printf("%s videos in the library%n", videoLibrary.getVideos().size());
  }

  public void showAllVideos() {
    List<Video> allVideos = videoLibrary.getVideos();
    System.out.println("Here's a list of all available videos:");
    allVideos.sort(Comparator.comparing(Video::getTitle));
    for (int i = 0; i < allVideos.size(); i++) {
      Video video = allVideos.get(i);
      System.out.print(video.getTitle());
      System.out.print(" (" + video.getVideoId() + ") ");
      String tags = video.getTags().toString();
      int index = tags.indexOf(",");
      if(index != -1){
      System.out.println(tags.substring(0, index) + tags.substring(index + 1, tags.length()));
      }
      else{
        System.out.println(video.getTags());
      }
    }
  }

  public void playVideo(String videoId) {
    Video video = videoLibrary.getVideo(videoId);
    if (video == null) {
      System.out.println("Cannot play video: Video does not exist");
    } else {
      if (playingVideo != null) {
        System.out.println("Stopping video: " + playingVideo.getTitle());
      }
      System.out.println("Playing video: " + video.getTitle());
      playingVideo = video;
      pausingVideo = null;
    }
  }

  public void stopVideo() {
    if (playingVideo == null) {
      System.out.println("Cannot stop video: No video is currently playing");
    } else {
      System.out.println("Stopping video: " + playingVideo.getTitle());
      playingVideo = null;
    }
  }

  public void playRandomVideo() {
    List<Video> allVideos = videoLibrary.getVideos();
    // Generate a random number
    int ranNum = (int) (Math.random() * (allVideos.size() - 1));
    if (playingVideo != null) {
      System.out.println("Stopping video: " + playingVideo.getTitle());
    }
    playingVideo = allVideos.get(ranNum);
    System.out.println("Playing video: " + playingVideo.getTitle());
    pausingVideo = null;
  }

  public void pauseVideo() {
    if (playingVideo == null) {
      System.out.println("Cannot pause video: No video is currently playing");
    } else if (pausingVideo != null) {
      System.out.println("Video already paused: " + pausingVideo.getTitle());
    } else {
      pausingVideo = playingVideo;
      System.out.println("Pausing video: " + playingVideo.getTitle());
    }
  }

  public void continueVideo() {
    if (playingVideo == null) {
      System.out.println("Cannot continue video: No video is currently playing");
    } else if (pausingVideo == null) {
      System.out.println("Cannot continue video: Video is not paused");
    } else {
      System.out.println("Continuing video: " + pausingVideo.getTitle());
      pausingVideo = null;
    }
  }

  public void showPlaying() {
    if (playingVideo == null) {
      System.out.println("No video is currently playing");
    } else if (pausingVideo != null) {
      System.out.print("Currently playing: ");
      System.out.print(playingVideo.getTitle());
      System.out.print(" (" + playingVideo.getVideoId() + ") ");
      String tags = playingVideo.getTags().toString();
      int index = tags.indexOf(",");
      System.out.print(tags.substring(0, index) + tags.substring(index + 1, tags.length()));
      System.out.println(" - PAUSED");
    } else {
      System.out.print("Currently playing: ");
      System.out.print(playingVideo.getTitle());
      System.out.print(" (" + playingVideo.getVideoId() + ") ");
      String tags = playingVideo.getTags().toString();
      int index = tags.indexOf(",");
      System.out.print(tags.substring(0, index) + tags.substring(index + 1, tags.length()));
    }
  }

  public void createPlaylist(String playlistName) {
    if (playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot create playlist: A playlist with the same name already exists");
    } else {
      playlists.put(playlistName.toLowerCase(), new VideoPlaylist(playlistName, new HashMap<>()));
      System.out.println("Successfully created new playlist: " + playlistName);
    }
  }

  public void addVideoToPlaylist(String playlistName, String videoId) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot add video to " + playlistName + ": Playlist does not exist");
    } else if (videoLibrary.getVideo(videoId) == null) {
      System.out.println("Cannot add video to " + playlistName + ": Video does not exist");
    } else if (playlists.get(playlistName.toLowerCase()).getVideos().containsKey(videoId)) {
      System.out.println("Cannot add video to " + playlistName + ": Video already added");
    } else {
      playlists.get(playlistName.toLowerCase()).getVideos().put(videoId, videoLibrary.getVideo(videoId));
      System.out.println("Added video to " + playlistName + ": " + videoLibrary.getVideo(videoId).getTitle());
    }
  }

  public void showAllPlaylists() {
    if (playlists.size() == 0) {
      System.out.println("No playlists exist yet");
    } else {
      Set<String> playlistNames = playlists.keySet();
      System.out.println("Showing all playlists:");
      for(int i = 0; i < playlistNames.size(); i++){
        System.out.println(playlists.get(i).getName());
      }
    }
  }

  public void showPlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot show playlist " + playlistName + ": Playlist does not exist");
    } else if (playlists.get(playlistName.toLowerCase()).getVideos().size() == 0) {
      System.out.println("Showing playlist: " + playlistName);
      System.out.println("  No videos here yet");
    } else {
      System.out.println("Showing playlist: " + playlistName);
      Map<String, Video> videos = playlists.get(playlistName.toLowerCase()).getVideos();
      System.out.println(videos.toString());
    }
  }

  public void removeFromPlaylist(String playlistName, String videoId) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot remove video from " + playlistName + ": Playlist does not exist");
    } else if (videoLibrary.getVideo(videoId) == null) {
      System.out.println("Cannot remove video from " + playlistName + ": Video does not exist");
    } else if (!playlists.get(playlistName.toLowerCase()).getVideos().containsKey(videoId)) {
      System.out.println("Cannot remove video from " + playlistName + ": Video is not in playlist");
    } else {
      playlists.get(playlistName.toLowerCase()).getVideos().remove(videoId);
      System.out.println("Successfully created new playlist: " + playlistName);
    }
  }

  public void clearPlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot clear playlist " + playlistName + ": Playlist does not exist");
    } else {
      playlists.replace(playlistName.toLowerCase(), null);
      System.out.println("Successfully removed all videos from " + playlistName);
    }
  }

  public void deletePlaylist(String playlistName) {
    if (!playlists.containsKey(playlistName.toLowerCase())) {
      System.out.println("Cannot delete playlist " + playlistName + ": Playlist does not exist");
    } else {
      playlists.remove(playlistName.toLowerCase());
      System.out.println("Deleted playlist: " + playlistName);
    }
  }

  public void searchVideos(String searchTerm) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> matchedVideos = new ArrayList();
    for (int i = 0; i < videos.size(); i++) {
      if (videos.get(i).getTitle().toLowerCase().contains(searchTerm.toLowerCase())) {
        matchedVideos.add(videos.get(i));
      }
    }
    if (matchedVideos.size() == 0) {
      System.out.println("No search results for " + searchTerm);
    } else {
      System.out.println("Here are the results for " + searchTerm + ":");
      matchedVideos.sort(Comparator.comparing(Video::getTitle));
      for (int i = 1; i <= matchedVideos.size(); i++) {
        Video matchedVideo = matchedVideos.get(i - 1);
        System.out.print("  " + i + ") ");
        System.out.print(matchedVideo.getTitle());
        System.out.print(" (" + matchedVideo.getVideoId() + ") ");
        String tags = matchedVideo.getTags().toString();
        int index = tags.indexOf(",");
        System.out.println(tags.substring(0, index) + tags.substring(index + 1, tags.length()));
      }
      Scanner input = new Scanner(System.in);
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      String number = input.nextLine();
      if (number.matches("[+-]?\\d*(\\.\\d+)?")) {
        int num = Integer.parseInt(number);
        if (num <= matchedVideos.size() && num > 0) {
          this.playVideo(matchedVideos.get(num - 1).getVideoId());
        }
      }
    }
  }

  public void searchVideosWithTag(String videoTag) {
    List<Video> videos = videoLibrary.getVideos();
    List<Video> matchedVideos = new ArrayList();
    for (int i = 0; i < videos.size(); i++) {
      if (videos.get(i).getTags().contains(videoTag.toLowerCase())) {
        matchedVideos.add(videos.get(i));
      }
    }
    if (matchedVideos.size() == 0) {
      System.out.println("No search results for " + videoTag);
    } else {
      matchedVideos.sort(Comparator.comparing(Video::getTitle));
      System.out.println("Here are the results for " + videoTag + ":");
      for (int i = 1; i <= matchedVideos.size(); i++) {
        Video matchedVideo = matchedVideos.get(i - 1);
        System.out.print("  " + i + ") ");
        System.out.print(matchedVideo.getTitle());
        System.out.print(" (" + matchedVideo.getVideoId() + ") ");
        String tags = matchedVideo.getTags().toString();
        int index = tags.indexOf(",");
        System.out.println(tags.substring(0, index) + tags.substring(index + 1, tags.length()));
      }
      Scanner input = new Scanner(System.in);
      System.out.println("Would you like to play any of the above? If yes, specify the number of the video.");
      System.out.println("If your answer is not a valid number, we will assume it's a no.");
      String number = input.nextLine();
      if (number.matches("[+-]?\\d*(\\.\\d+)?")) {
        int num = Integer.parseInt(number);
        if (num <= matchedVideos.size() && num > 0) {
          this.playVideo(matchedVideos.get(num - 1).getVideoId());
        }
      }
    }
  }

  public void flagVideo(String videoId) {
    System.out.println("flagVideo needs implementation");
  }

  public void flagVideo(String videoId, String reason) {
    System.out.println("flagVideo needs implementation");
  }

  public void allowVideo(String videoId) {
    System.out.println("allowVideo needs implementation");
  }
}