import boto3

s3_client = boto3.client('s3')

class MyClass:
    """A simple example class"""
    i = 12345

    def f(self):
        return 'hello world'
