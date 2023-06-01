

import json
import socket

import unittest

HOST: str = '127.0.0.1'
PORT: int = 9090

def wait_load(connection: socket.socket):
    while True:
        # Send data to the server
        data = "{ }"
        connection.send(data.encode())

        # Receive and decode the response
        respuesta = connection.recv(1024).decode()
        respuesta = json.loads(respuesta)
        
        if respuesta['status'] == 200: break

def test_server(connection: socket.socket):
    data = {'all'   : False, 
            "seeds" : ["1Bv0Yl01xBDZD4OQP93fyl", "7k6IzwMGpxnRghE7YosnXT","4omisSlTk6Dsq2iQD7MA07","7xYnUQigPoIDAMPVK79NEq","6d8A5sAx9TfdeseDvfWNHd","4pmc2AxSEq6g7hPVlJCPyP"], 
            "filtered"  : ["6ToAD7ajJidQTDn72OncDG","6yEfq48XkEzCg0TwY6Hhkb","1ZgfAxHQCXLt8o1VXEHHAt","4b1sQpvL7QVgamRZ74F1oA","0AO2yQNGcH0ASHHU5lS4lT","0g1AmSKokPboFrxmG1dxKx","5S5gGOsUarzRXKoaSstwba","4z8xFgX0Xq1T9xXomoDLJ2","4Y6Voc2xpc4lPD5iKpN4o2","2fRojxstKd6Tz9VziOeo9n","2BPXILn0MqOe5WroVXlvN1","1zl7VGjTBriRXgdHEb6z1e","2MSjhkw47sGrjbZhColyVx","4ZSLc2F15wKVPjyLt169tt","67BbMuItfrHmaNqDxBPF7c","0yiigrFvNj02eIlqM8ZGXf","0mtuqz4rcMYCKjF1cjmlu6","7dkX68UnlbaCBWNDWSrthP","2sdf2x4YLaHb1k9HtE9cBs","00z4wF0iJsp6GwDkQxkGs6","0k6ItoyE50QuW50nGYLO2v","61NMqZzdUmRHw1KdS1honh","0PibTyOTrNsVfQReWmPhc8","3JBnDOUd18QKjDqSYuOfpm","1dzuJ9z66RXuDV3t3gQrFI","3MdJSXjBarAYuuJ7rjJLDk","2LowwiemmGMzzNSH1PJprK"]
            } 

    try: 
        connection.send(json.dumps(data).encode())
        response = connection.recv(2500).decode()
        response = json.loads(response)
        assert response['status'] == 200
        print(response["uris"])
        print ( "\n\n\n\n\n\n")

    except Exception as err:
        print(err)


def test_server_all(connection: socket.socket):
    data = {'all'  : True, 
            "seeds" : ["1Bv0Yl01xBDZD4OQP93fyl", "7k6IzwMGpxnRghE7YosnXT","4omisSlTk6Dsq2iQD7MA07","7xYnUQigPoIDAMPVK79NEq","6d8A5sAx9TfdeseDvfWNHd","4pmc2AxSEq6g7hPVlJCPyP"], 
            "urls" : []
            }
    try: 
        connection.send(json.dumps(data).encode())
        response = connection.recv(2500).decode()
        print(response)
        response = json.loads(response)
        assert response['status'] == 202
        print(response["uris"])

    except Exception as err:
        print(err)

if __name__ == '__main__':
    conn = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    conn.connect((HOST, PORT))
    wait_load(conn)
    test_server(conn)
    test_server_all(conn)
    conn.close()
