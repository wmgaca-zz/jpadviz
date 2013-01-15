import os
import thread
import subprocess
import time

names = ['run-server.bat', 'run-dummy-client.bat', 'run-visualiser.bat']
pids = []

def print_usage():
    usage = '''Usage:
    r, restart
        Restart app.

    k, kill
        Kill all processes.

    q, quit, exit
        Close all and exit script/

    h, help:
        Print this guide.
    '''
    print usage

def run(bat):
    handle = subprocess.Popen(bat, shell=False)
    global pids
    pids.append(handle.pid)

def run_all():
    for name in names:
        thread.start_new_thread(run, (name,))
        time.sleep(1)

def kill_all():
    for pid in pids:
        subprocess.Popen("taskkill /F /T /PID %i" % pid, shell=True)
        time.sleep(1)

def main():
    run_all()

    while True:
        user_input = raw_input("> ").strip()

        if user_input in ['h', 'help']:
            print_usage()
        elif user_input in ['q', 'quit', 'exit']:
            kill_all()
            return
        elif user_input in ['k', 'kill']:
            kill_all()
        elif user_input in ['r', 'restart']:
            kill_all()
            run_all()

if __name__ == '__main__':
    main()
