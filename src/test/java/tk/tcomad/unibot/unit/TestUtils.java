package tk.tcomad.unibot.unit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import tk.tcomad.unibot.entity.Bot;
import tk.tcomad.unibot.entity.restentity.Week;
import tk.tcomad.unibot.entity.restentity.botdata.App;
import tk.tcomad.unibot.entity.restentity.botdata.Book;
import tk.tcomad.unibot.entity.restentity.botdata.Lecture;
import tk.tcomad.unibot.entity.restentity.botdata.Lesson;
import tk.tcomad.unibot.entity.restentity.botdata.Student;
import tk.tcomad.unibot.entity.restentity.botdata.Teacher;
import tk.tcomad.unibot.repository.AppRepository;
import tk.tcomad.unibot.repository.BookRepository;
import tk.tcomad.unibot.repository.LectureRepository;
import tk.tcomad.unibot.repository.LessonRepository;
import tk.tcomad.unibot.repository.StudentRepository;
import tk.tcomad.unibot.repository.TeacherRepository;
import tk.tcomad.unibot.repository.WeekRepository;

public class TestUtils {

  static Update mockCallBackUpdate(String text) {
    Update update = mock(Update.class);
    when(update.hasMessage()).thenReturn(true);
    Message message = mock(Message.class);
    when(message.getChatId()).thenReturn(TelegramBotTest.CHAT_ID);
    when(update.getMessage()).thenReturn(message);
    CallbackQuery callbackQuery = mock(CallbackQuery.class);
    when(callbackQuery.getData()).thenReturn(text);
    when(update.getCallbackQuery()).thenReturn(callbackQuery);
    return update;
  }

  static AppRepository mockAppRepository(Bot botEntity) {
    AppRepository appRepository = mock(AppRepository.class);
    List<App> apps = new ArrayList<>();
    App app = new App();
    app.setId(1L);
    app.setBot(botEntity);
    app.setName("TestApp");
    app.setUrl("https://test.com/app");
    apps.add(app);
    when(appRepository.findAllByBot(botEntity)).thenReturn(apps);
    when(appRepository.findAppById(1L)).thenReturn(Optional.of(app));
    return appRepository;
  }

  static BookRepository mockBookRepository(Bot botEntity) {
    BookRepository bookRepository = mock(BookRepository.class);
    List<Book> books = new ArrayList<>();
    Book book = new Book();
    book.setId(1L);
    book.setBot(botEntity);
    book.setName("TestBook");
    book.setUrl("https://test.com/book");
    books.add(book);
    when(bookRepository.findAllByBot(botEntity)).thenReturn(books);
    when(bookRepository.findBookById(1L)).thenReturn(Optional.of(book));
    return bookRepository;
  }

  static LectureRepository mockLectureRepository(Bot botEntity) {
    LectureRepository lectureRepository = mock(LectureRepository.class);
    List<Lecture> lectures = new ArrayList<>();
    Lecture lecture = new Lecture();
    lecture.setId(1L);
    lecture.setBot(botEntity);
    lecture.setName("TestLecture");
    lecture.setUrl("https://test.com/lecture");
    lectures.add(lecture);
    when(lectureRepository.findAllByBot(botEntity)).thenReturn(lectures);
    when(lectureRepository.findLectureById(1L)).thenReturn(Optional.of(lecture));
    return lectureRepository;
  }

  static StudentRepository mockStudentRepository(Bot botEntity) {
    StudentRepository studentRepository = mock(StudentRepository.class);
    List<Student> students = new ArrayList<>();
    Student student = new Student();
    student.setId(1L);
    student.setBot(botEntity);
    student.setName("TestStudent");
    student.setData("+38000000000");
    students.add(student);
    when(studentRepository.findAllByBot(botEntity)).thenReturn(students);
    when(studentRepository.findStudentById(1L)).thenReturn(Optional.of(student));
    return studentRepository;
  }

  static TeacherRepository mockTeacherRepository(Bot botEntity) {
    TeacherRepository teacherRepository = mock(TeacherRepository.class);
    List<Teacher> teachers = new ArrayList<>();
    Teacher teacher = new Teacher();
    teacher.setId(1L);
    teacher.setBot(botEntity);
    teacher.setName("TestTeacher");
    teacher.setData("+38000000000");
    teachers.add(teacher);
    when(teacherRepository.findAllByBot(botEntity)).thenReturn(teachers);
    when(teacherRepository.findTeacherById(1L)).thenReturn(Optional.of(teacher));
    return teacherRepository;
  }

  static LessonRepository mockLessonRepository(Bot botEntity) {
    LessonRepository lessonRepository = mock(LessonRepository.class);
    List<Lesson> lessons = new ArrayList<>();
    Lesson lesson = new Lesson();
    lesson.setId(1L);
    lesson.setBot(botEntity);
    lesson.setDay(1);
    lesson.setLessonOfFirstWeek("TestLesson");
    lesson.setLessonOfSecondWeek("TestLesson");
    lessons.add(lesson);
    when(lessonRepository.findAllByBotAndDay(botEntity, 1)).thenReturn(lessons);
    when(lessonRepository.findLessonById(1L)).thenReturn(Optional.of(lesson));
    return lessonRepository;
  }

  static WeekRepository mockWeekRepository(Bot botEntity) {
    WeekRepository weekRepository = mock(WeekRepository.class);
    Week week = new Week();
    week.setBot(botEntity);
    week.setDate("01.09.2020");
    when(weekRepository.findByBot(botEntity)).thenReturn(Optional.of(week));
    when(weekRepository.findById(1L)).thenReturn(Optional.of(week));
    return weekRepository;
  }
}
