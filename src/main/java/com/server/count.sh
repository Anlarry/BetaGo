
echo net
find net -regex ".*\\.\(java\)" | xargs cat | wc -l

echo start
find start -regex ".*\\.\(java\)" | xargs cat | wc -l

echo tools
find tools -regex ".*\\.\(java\)" | xargs cat | wc -l

echo ui
find ui -regex ".*\\.\(java\)" | xargs cat | wc -l