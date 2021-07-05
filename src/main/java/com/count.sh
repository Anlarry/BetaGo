echo client
find client -regex ".*\\.\(java\)" | xargs cat | wc -l
echo server
find server -regex ".*\\.\(java\)" | xargs cat | wc -l
