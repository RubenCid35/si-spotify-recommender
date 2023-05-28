import socket

def test_server():
    host = '127.0.0.1'
    port = 9090

    # Create a socket and connect to the server
    cliente = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    cliente.connect((host, port))


    for _ in range(20):
        # Send data to the server
        data = "{ }"
        cliente.send(data.encode())

        # Receive and decode the response
        respuesta = cliente.recv(1024).decode()

        # Print the response
        print("Server response:", respuesta)
        if "200" in respuesta: break

    # Close the connection
    cliente.close()

if __name__ == '__main__':
    test_server()