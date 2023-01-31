echo "Rebuilding Discord Bot..." &&
  echo ""

echo "Running Docker Commands..." &&
  echo ""

# Remove Previous Containers
docker container stop discord-bot &&
  docker container rm discord-bot &&
  echo "Stopped and Removed previous Container(s)." &&
  echo ""

# Build New Image & Run
docker build -t discord-bot . &&
  docker run --name discord-bot -it -d -v livechats.json:/app/livechats.json discord-bot &&
  echo "" &&
  echo "Discord Bot successfully rebuilt." &&
  echo ""