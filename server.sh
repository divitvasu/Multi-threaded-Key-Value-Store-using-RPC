PROJECT_NETWORK='host'
SERVER_IMAGE='server_app'
SERVER_CONTAINER='server'

CLIENT1_IMAGE='client1'
CLIENT1_CONTAINER='client-1'

CLIENT2_IMAGE='client2'
CLIENT2_CONTAINER='client-2'


# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $SERVER_CONTAINER 2> /dev/null && docker container rm $SERVER_CONTAINER 2> /dev/null
docker container stop $CLIENT1_CONTAINER 2> /dev/null && docker container rm $CLIENT1_CONTAINER 2> /dev/null
docker container stop $CLIENT2_CONTAINER 2> /dev/null && docker container rm $CLIENT2_CONTAINER 2> /dev/null
#docker network rm $PROJECT_NETWORK 2> /dev/null

# only cleanup
if [ "$1" == "cleanup-only" ]
then
  exit
fi

# create a custom virtual network
#echo "----------creating a virtual network----------"
#docker network create $PROJECT_NETWORK

# build the images from Dockerfile
echo "----------Building images----------"
docker build -t $CLIENT1_IMAGE --target client1-build .
docker build -t $CLIENT2_IMAGE --target client2-build .
docker build -t $SERVER_IMAGE --target server-build .

# run the image and open the required ports
echo "----------Running server app----------"
docker run --rm --name $SERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE java Server_app "$1"
