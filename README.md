# AltTube - Video Surfing Application
AltTube is a mobile application similar to TikTok and YouTube Shorts that allows users to browse, interact with, and manage video content through a seamless vertical scrolling interface.

## Features
### Core Video Experience
- Vertical Swiping: Navigate through videos with vertical scrolling
- **Video Controls**:
  - Play/Pause with single tap
  - Double tap left/right to seek backward/forward
  - Mute/Unmute toggle

## Content Management
- Video Upload: Upload videos directly from device
- Personal Profile: View and manage uploaded content
- Delete Videos: Remove uploaded 

### User Interactions
- **Favorites System**:
  - Mark videos as favorites with double tap
  - View favorite videos in dedicated tab
- Like System
- Share video links with other apps
- Anonymous Authentication: Automatic anonymous user accounts

## Technical Implementation
### Architecture & Libraries
- UI Framework: Jetpack Compose
- Video Playback: ExoPlayer through Media3
- Backend: Supabase for:
  - Storage (videos)
  - Authentication
  - User data (PostgreSQL)
- Dependency Injection: Hilt
- Navigation: Jetpack Navigation Compose

## Key Components
- VideoFeedItem: Core video player component with gesture controls
- MainScaffold: Main UI container with bottom navigation
- UploadScreen: Video upload interface with preview
- ProfileScreen: User content management interface

## Building and Running
### Prerequisites
1. Android Studio Hedgehog or newer
2. Supabase account and project set up
3. Android device/emulator (API 24+)

### Setup
1. Clone the repository
2. Create local.properties (if it does not exist) in root directory and add:
```
SUPABASE_URL="your_supabase_url"
SUPABASE_KEY="your_supabase_anon_key"
```
3. Create Supabase storage bucket named "videos"
4. Set up Supabase database with table:
```
create table users (
  id uuid default gen_random_uuid() primary key,
  supabase_user_id text unique,
  uploads text[],
  liked text[],
  favorites text[]
);
```

### Run
- Open project in Android Studio
- Sync project with Gradle files
- Build and run on device/emulator

## Assumptions & Design Decisions
- Anonymous Authentication: Used to simplify user management while maintaining per-user features
- Video Format Support: Relies on device's native codec support
- Storage Limitations: Assumes reasonable video sizes and durations

## Additional Features
- Double-Tap Seeking: Quick 5-second jumps with visual feedback
- Upload Progress: Visual progress indicator during uploads
- Error Recovery: Retry options for failed uploads/loads
- Thumbnail Generation: Auto-generated video previews
- Share Integration: Native Android sharing support
