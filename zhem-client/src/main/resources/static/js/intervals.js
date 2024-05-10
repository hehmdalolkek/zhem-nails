document.addEventListener('DOMContentLoaded', () => {

    closeAllTimeInputs();
    closeAllUpdateTimes();

    const openTimeButtons = document.querySelectorAll('.open-time-btn');
    openTimeButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            const target = e.target.parentElement;
            const timeInput = target.parentElement.querySelector('.time-input');
            const openTimeBtn = target.parentElement.querySelector('.open-time-btn');
            const closeTimeBtn = target.parentElement.querySelector('.close-time-btn');

            closeAllTimeInputs();
            closeAllUpdateTimes();

            timeInput.style.display = 'block';
            openTimeBtn.style.display = 'none'
            closeTimeBtn.style.display = 'block';
        });
    });

    const closeTimeButtons = document.querySelectorAll('.close-time-btn');
    closeTimeButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            closeAllTimeInputs();
        });
    });

    const openUpdateTimeButtons = document.querySelectorAll('.open-update-time-btn');
    openUpdateTimeButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            const target = e.target.parentElement;
            const updateTime = target.parentElement.querySelector('.update-time');
            const currentTime = target.parentElement.querySelector('.current-time');
            const deleteTime = target.parentElement.querySelector('.delete-time');
            const openUpdateTimeBtn = target.parentElement.querySelector('.open-update-time-btn');
            const closeUpdateTimeBtn = target.parentElement.querySelector('.close-update-time-btn');

            closeAllUpdateTimes();
            closeAllTimeInputs();

            currentTime.style.display = 'none';
            if (deleteTime != null) {
                deleteTime.style.display = 'none';
            }
            updateTime.style.display = 'block';
            openUpdateTimeBtn.style.display = 'none'
            closeUpdateTimeBtn.style.display = 'block';
        });
    });

    const closeUpdateTimeButtons = document.querySelectorAll('.close-update-time-btn');
    closeUpdateTimeButtons.forEach(button => {
        button.addEventListener('click', (e) => {
            closeAllUpdateTimes();
        });
    });

    function closeAllTimeInputs() {
        document.querySelectorAll('.time-input').forEach(input => {
            input.style.display = 'none';
        });
        document.querySelectorAll('.close-time-btn').forEach(button => {
            button.style.display = 'none';
        });
        document.querySelectorAll('.open-time-btn').forEach(button => {
            button.style.display = 'block';
        });
    }

    function closeAllUpdateTimes() {
        document.querySelectorAll('.update-time').forEach(input => {
            input.style.display = 'none';
        });
        document.querySelectorAll('.close-update-time-btn').forEach(button => {
            button.style.display = 'none';
        });
        document.querySelectorAll('.open-update-time-btn').forEach(button => {
            button.style.display = 'block';
        });
        document.querySelectorAll('.current-time').forEach(button => {
            button.style.display = 'block';
        });
        document.querySelectorAll('.delete-time').forEach(button => {
            button.style.display = 'block';
        });
    }
});